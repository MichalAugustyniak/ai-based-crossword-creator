package com.aibasedcrosswordcreator.crosswordservice.service;

import com.aibasedcrosswordcreator.crosswordservice.dto.*;
import com.aibasedcrosswordcreator.crosswordservice.mapper.CrosswordResponseMapper;
import com.aibasedcrosswordcreator.crosswordservice.mapper.CrosswordResponseMapperFactory;
import com.aibasedcrosswordcreator.crosswordservice.exception.*;
import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGenerator;
import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGeneratorV1;
import com.aibasedcrosswordcreator.crosswordservice.generator.WordOrientation;
import com.aibasedcrosswordcreator.crosswordservice.mapper.CrosswordMapper;
import com.aibasedcrosswordcreator.crosswordservice.model.*;
import com.aibasedcrosswordcreator.crosswordservice.repository.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Validated
public class CrosswordService {
    private final ClueRepository clueRepository;
    private final LanguageRepository languageRepository;
    private final CrosswordRepository crosswordRepository;
    private final WordService wordService;
    private final ThemeRepository themeRepository;
    private final WordRepository wordRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final ProviderRepository providerRepository;
    private final ModelRepository modelRepository;
    private final ProviderModelRepository providerModelRepository;
    private final Logger log = LoggerFactory.getLogger(CrosswordService.class);
    private final int iterations;
    private final int attemptsLimit;

    public CrosswordService(
            ClueRepository clueRepository,
            LanguageRepository languageRepository,
            CrosswordRepository crosswordRepository,
            ThemeRepository themeRepository,
            WordRepository wordRepository,
            CoordinatesRepository coordinatesRepository,
            WordService wordService,
            ProviderModelRepository providerModelRepository,
            ProviderRepository providerRepository,
            ModelRepository modelRepository,
            @Value("${crossword-service.iterations}") int iterations,
            @Value("${crossword-service.attempts-limit}") int attemptsLimit
    ) {
        this.clueRepository = clueRepository;
        this.languageRepository = languageRepository;
        this.crosswordRepository = crosswordRepository;
        this.themeRepository = themeRepository;
        this.wordRepository = wordRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.wordService = wordService;
        this.providerModelRepository = providerModelRepository;
        this.providerRepository = providerRepository;
        this.modelRepository = modelRepository;
        this.iterations = iterations;
        this.attemptsLimit = attemptsLimit;
    }

    static List<WordDTO> crosswordWordListToWordDTOList(@NotNull List<@NotNull Clue> clues, @NotNull com.aibasedcrosswordcreator.crosswordservice.generator.Crossword crossword) {
        List<WordDTO> wordDTOS = new ArrayList<>();
        for (var word : crossword.getWords()) {
            Clue clue = clues.stream().filter(c -> c.getWord()
                            .getText()
                            .equalsIgnoreCase(word.getContent()))
                    .findAny().orElseThrow(
                            () -> new WordNotFoundException("Missing word while creating response dto")
                    );
            WordDTO wordDTO = getWordDTO(word, clue);
            wordDTOS.add(wordDTO);
        }
        return wordDTOS;
    }

    static WordDTO getWordDTO(@NotNull com.aibasedcrosswordcreator.crosswordservice.generator.Word word, @NotNull Clue clue) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setText(clue.getWord().getText());
        wordDTO.setIdentifier(word.getIdentifier());
        if (word.getOrientation() == WordOrientation.HORIZONTAL) {
            wordDTO.setCoordinates(new WordDTO.CoordinatesDTO(word.getStartPoint().hIndex(), word.getStartPoint().wIndex() - 1));
            wordDTO.setOrientation("h");
        } else {
            wordDTO.setCoordinates(new WordDTO.CoordinatesDTO(word.getStartPoint().hIndex() - 1, word.getStartPoint().wIndex()));
            wordDTO.setOrientation("v");
        }
        wordDTO.setClue(clue.getText().trim());
        return wordDTO;
    }

    static ProviderModel findProviderModel(
            @NotNull String providerName,
            @NotNull String modelName,
            @NotNull ProviderRepository providerRepository,
            @NotNull ModelRepository modelRepository,
            @NotNull ProviderModelRepository providerModelRepository
    ) {
        if (providerName == null && modelName == null) {
            return null;
        }
        Provider provider = providerRepository.findByName(providerName).orElseGet(() -> {
            Provider newProvider = Provider.builder().name(providerName).description(providerName + "'s autogenerated description").build();
            return providerRepository.save(newProvider);
        });
        Model model = modelRepository.findByName(modelName).orElseGet(() -> {
            Model newModel = Model.builder().name(modelName).description(modelName + "'s autogenerated description").build();
            return modelRepository.save(newModel);
        });
        return providerModelRepository.findByProviderAndModel(provider, model).orElseGet(() -> {
            ProviderModel newProviderModel = ProviderModel.builder().provider(provider).model(model).build();
            return providerModelRepository.save(newProviderModel);
        });
    }

    public GetCrosswordsResponseDTO getCrosswords(@NotNull GetCrosswordsRequestDTO request) {
        log.info("Fetching crosswords...");
        Specification<Crossword> specification = Specification.where(null);
        try {
            Specification<Crossword> hasCreator;
            if (request.creator() != null && request.creator().equals("null")) {
                hasCreator = (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("creator"));
            } else {
                hasCreator = (root, query, criteriaBuilder) ->
                        request.creator() != null && request.creator().isEmpty() ? null : criteriaBuilder.equal(root.get("creator"), request.creator());
            }
            specification = specification.and(hasCreator);
            if (request.language() != null) {
                Language language = languageRepository.findByText(request.language()).orElseThrow(
                        () -> new LanguageNotFoundException("Language not found: " + request.language())
                );
                Specification<Crossword> hasLanguage = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("language"), language);
                specification = specification.and(hasLanguage);
            }
            if (request.provider() != null) {
                Provider provider = providerRepository.findByName(request.provider())
                        .orElseThrow(() -> new ProviderNotFoundException("Provider not found: " + request.provider()));
                Specification<Crossword> hasProvider = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.join("providerModel").get("provider"), provider);
                specification = specification.and(hasProvider);
            }
            if (request.model() != null) {
                Model model = modelRepository.findByName(request.model())
                        .orElseThrow(() -> new ModelNotFoundException("Specified model not found"));
                Specification<Crossword> hasModel = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.join("providerModel").get("model"), model);
                specification = specification.and(hasModel);
            }
            if (request.height() != null) {
                Specification<Crossword> hasHeight = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("height"), request.height());
                specification = specification.and(hasHeight);
            }
            if (request.width() != null) {
                Specification<Crossword> hasWidth = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("width"), request.width());
                specification = specification.and(hasWidth);
            }
            Page<Crossword> crosswords = crosswordRepository.findAll(specification, PageRequest.of(request.page(), request.quantity()));
            List<CrosswordResponse> crosswordDTOS = crosswords.get().map(CrosswordMapper::toCrosswordResponse).toList();
            log.debug("Fetched {} crosswords", crosswords.toList().size());
            return new GetCrosswordsResponseDTO(
                    crosswords.getTotalPages(),
                    crosswords.getNumberOfElements(),
                    crosswords.getTotalElements(),
                    crosswordDTOS
            );
        } catch (LanguageNotFoundException | ProviderNotFoundException | ModelNotFoundException e) {
            return new GetCrosswordsResponseDTO(
                    1,
                    0,
                    0,
                    Collections.emptyList()
            );
        }
    }

    public CrosswordResponse generateWithoutAi(@NotNull GenerateCrosswordRequest request) {
        Language language = languageRepository.findByText(request.language())
                .orElseThrow(() -> new LanguageNotFoundException(String.format("Language of name %s not found", request.language())));
        Theme theme = themeRepository.findByName(request.theme())
                .orElseThrow(() -> new ThemeNotFoundException(String.format("Theme of name %s not found", request.theme())));

        List<Clue> clues = clueRepository.findByThemeAndLanguageFetchedWord(theme, language, Math.max(request.height(), request.width()));
        if (clues.isEmpty()) {
            throw new WordNotFoundException(String.format("Words not found for theme %s", request.theme()));
        }
        List<String> wordStrings = new ArrayList<>(clues.stream().map(clue -> clue.getWord().getText()).toList());
        CrosswordGenerator crosswordGenerator = new CrosswordGeneratorV1();
        var crossword = crosswordGenerator.generateCrossword(
                wordStrings,
                request.height(),
                request.width(),
                request.language(),
                request.theme()
        );
        for (int i = 0; i < iterations; i++) {
            var crossword1 = crosswordGenerator.generateCrossword(
                    wordStrings,
                    request.height(),
                    request.width(),
                    request.language(),
                    request.theme()
            );
            if (crossword1.getWords().size() > crossword.getWords().size()) {
                crossword = crossword1;
            }
        }
        Crossword savedCrossword = saveCrossword(crossword, request);
        List<WordDTO> wordDTOS = crosswordWordListToWordDTOList(clues, crossword);
        return new CrosswordResponse(
                savedCrossword.getUuid(),
                request.theme(),
                request.height(),
                request.width(),
                request.language(),
                savedCrossword.getCreator(),
                crossword.bodyToString(),
                wordDTOS
        );
    }

    public CrosswordResponse generateWithAi(@NotNull GenerateCrosswordRequest request) {
        String language = request.language();
        String providerName = request.provider();
        String modelName = request.model();
        int maxWordLength = Math.max(request.height(), request.width()) - 1;
        Optional<Theme> optionalTheme = themeRepository.findByThemeName(request.theme());
        Optional<Language> optionalLanguage = languageRepository.findByText(language);
        ProviderModel providerModel = findProviderModel(providerName, modelName, providerRepository, modelRepository, providerModelRepository);
        List<Clue> cluesFromDatabase = new ArrayList<>();
        if (optionalLanguage.isPresent() && optionalTheme.isPresent()) {
            cluesFromDatabase = clueRepository.findByThemeAndLanguageAndProviderModelFetchedWord(
                    optionalTheme.get(),
                    optionalLanguage.get(),
                    providerModel,
                    maxWordLength
            );
        }
        List<com.aibasedcrosswordcreator.crosswordservice.generator.Crossword> crosswords = new LinkedList<>();

        int currentAttempt = 0;
        while (currentAttempt < attemptsLimit) {
            if (cluesFromDatabase.size() < request.height() * request.width()) {
                try {
                    wordService.generateWords(
                            new GenerateWordsRequestDTO(
                                    providerName,
                                    modelName,
                                    new GenerateWordsDetailsDTO(
                                            request.theme(),
                                            language,
                                            request.height() * request.width(),
                                            maxWordLength
                                    )
                            )
                    );
                    optionalTheme = themeRepository.findByName(request.theme());
                    optionalLanguage = languageRepository.findByText(language);
                    Theme theme = optionalTheme.orElseThrow(() -> new ThemeNotFoundException("Theme not found with name: " + request.theme()));
                    Language languageFromDB = optionalLanguage.orElseThrow(() -> new LanguageNotFoundException("Language not found with name: " + language));
                    cluesFromDatabase = clueRepository.findByThemeAndLanguageAndProviderModelFetchedWord(
                            theme,
                            languageFromDB,
                            providerModel
                    );
                } catch (Exception e) {
                    long delay = (long) Math.pow(2, ++currentAttempt);
                    try {
                        TimeUnit.SECONDS.sleep(delay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }
        List<String> uniqueWords = cluesFromDatabase.stream().map(c -> c
                .getWord()
                .getText()).toList();
        CrosswordGenerator crosswordGenerator = new CrosswordGeneratorV1();
        for (int i = 0; i < iterations; i++) {
            com.aibasedcrosswordcreator.crosswordservice.generator.Crossword crossword =
                    crosswordGenerator.generateCrossword(uniqueWords, request.height(), request.width(), language, request.theme());
            crosswords.add(crossword);
        }
        com.aibasedcrosswordcreator.crosswordservice.generator.Crossword finalCrossword = crosswords.get(0);
        for (com.aibasedcrosswordcreator.crosswordservice.generator.Crossword crossword : crosswords) {
            if (crossword.getWords().size() > finalCrossword.getWords().size()) {
                finalCrossword = crossword;
            } else if (
                    crossword.getWords().size() == finalCrossword.getWords().size() &&
                            crossword.checkAllSides()
            ) {
                finalCrossword = crossword;
            }
        }
        List<Clue> clues = clueRepository.findAllByTextCollectionAndThemeAndLanguageAndProviderModelFetchedWord(
                finalCrossword.getWords().stream().map(com.aibasedcrosswordcreator.crosswordservice.generator.Word::getContent).collect(Collectors.toList()),
                optionalTheme.orElseThrow(() -> new ThemeNotFoundException("Theme not found with name: " + request.theme())),
                optionalLanguage.orElseThrow(() -> new LanguageNotFoundException("Language not found with name: " + language)),
                providerModel
        );
        Crossword crossword = saveCrossword(finalCrossword, request);
        List<WordDTO> wordDTOS = crosswordWordListToWordDTOList(clues, finalCrossword);
        log.debug("Crossword generated with theme: {}, provider: {}, model: {}", request.theme(), providerName, modelName);
        return new CrosswordResponse(
                crossword.getUuid(),
                request.theme(),
                request.height(),
                request.width(),
                language,
                crossword.getCreator(),
                finalCrossword.bodyToString(),
                wordDTOS
        );
    }

    public Crossword saveCrossword(@NotNull com.aibasedcrosswordcreator.crosswordservice.generator.Crossword crossword, @NotNull GenerateCrosswordRequest request) {
        String id = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            JwtAuthenticationToken token = (JwtAuthenticationToken) securityContext.getAuthentication();
            if (token.isAuthenticated()) {
                id = (String) token.getTokenAttributes().get(JwtClaimNames.SUB);
            }
        } catch (ClassCastException ignored) {

        }
        Theme theme = themeRepository.findByThemeName(request.theme())
                .orElseThrow(() -> new ThemeNotFoundException(String.format("Theme %s not found", request.theme())));
        Language language = languageRepository.findByText(request.language())
                .orElseThrow(() -> new LanguageNotFoundException(String.format("Language %s not found", request.language())));
        String providerName = request.provider();
        String modelName = request.model();
        ProviderModel providerModel = findProviderModel(providerName, modelName, providerRepository, modelRepository, providerModelRepository);
        com.aibasedcrosswordcreator.crosswordservice.model.Crossword crosswordModel = new com.aibasedcrosswordcreator.crosswordservice.model.Crossword();
        UUID uuid = UUID.randomUUID();
        crosswordModel.setLanguage(language);
        crosswordModel.setTheme(theme);
        crosswordModel.setHeight(request.height());
        crosswordModel.setWidth(request.width());
        crosswordModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        crosswordModel.setCreator(id);
        crosswordModel.setType("V1");
        crosswordModel.setProviderModel(providerModel);
        crosswordModel.setUuid(uuid);
        List<String> words = new ArrayList<>(crossword.getWords().stream().map(com.aibasedcrosswordcreator.crosswordservice.generator.Word::getContent).toList());
        List<Word> foundWords = new LinkedList<>();
        List<Clue> foundClues = providerModel != null ? clueRepository.findAllByTextCollectionAndThemeAndLanguageAndProviderModelFetchedWord(words, theme, language, providerModel)
                : clueRepository.findAllByTextCollectionAndThemeAndLanguageProviderModelIsNullFetchedWord(words, theme, language);
        Map<String, Clue> clueByWordStringMap = new HashMap<>();
        foundClues.forEach(c -> {
            foundWords.add(c.getWord());
            clueByWordStringMap.put(c.getWord().getText().toLowerCase(), c);
        });
        List<Coordinates> coordinatesList = new LinkedList<>();
        for (com.aibasedcrosswordcreator.crosswordservice.generator.Word word : crossword.getWords()) {
            Clue clue = clueByWordStringMap.get(word.getContent());
            Optional.ofNullable(clue)
                    .orElseThrow(() -> new ClueNotFoundException("Cannot save the crossword. Missing clue for word: " + word.getContent()));
            Coordinates coordinates = new Coordinates();
            coordinates.setX(word.getStartPoint().wIndex());
            coordinates.setY(word.getStartPoint().hIndex());
            coordinates.setCrossword(crosswordModel);
            coordinates.setClue(clue);
            coordinates.setOrientation(String.valueOf(word.getOrientation().toString().charAt(0)).toLowerCase());
            coordinates.setIdentifier(word.getIdentifier());
            coordinatesList.add(coordinates);
        }
        coordinatesRepository.saveAll(coordinatesList);
        wordRepository.saveAll(foundWords);
        crosswordRepository.save(crosswordModel);
        return crosswordModel;
    }

    public CrosswordResponse getCrosswordByUuid(@NotNull CrosswordRequest request) {
        Crossword crossword = crosswordRepository.findByUuidFetchedThemeAndCoordinatesAndClueAndWordAndLanguage(request.uuid())
                .orElseThrow(() -> new CrosswordNotFoundException("Crossword not found for the given uuid"));
        log.info("Found crossword with uuid: {}", crossword.getUuid());
        CrosswordResponseMapper crosswordDTOBuilder = CrosswordResponseMapperFactory.create(crossword.getType());
        return crosswordDTOBuilder.map(crossword);
    }
}
