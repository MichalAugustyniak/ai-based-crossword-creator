package com.aibasedcrosswordcreator.crosswordservice.service;

import com.aibasedcrosswordcreator.crosswordservice.dto.*;
import com.aibasedcrosswordcreator.crosswordservice.exception.*;
import com.aibasedcrosswordcreator.crosswordservice.helper.ClueHelper;
import com.aibasedcrosswordcreator.crosswordservice.helper.StandardCrosswordHelper;
import com.aibasedcrosswordcreator.crosswordservice.mapper.StandardCrosswordResponseMapper;
import com.aibasedcrosswordcreator.crosswordservice.mapper.StandardCrosswordResponseMapperFactory;
import com.aibasedcrosswordcreator.crosswordservice.model.*;
import com.aibasedcrosswordcreator.crosswordservice.registry.StandardCrosswordGeneratorRegistry;
import com.aibasedcrosswordcreator.crosswordservice.repository.*;
import com.aibasedcrosswordcreator.crosswordservice.util.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.aibasedcrosswordcreator.crosswordservice.util.ProviderModelUtil.findProviderModel;

@Service
@Validated
public class StandardCrosswordService {
    private final ClueRepository clueRepository;
    private final LanguageRepository languageRepository;
    private final StandardCrosswordRepository crosswordRepository;
    private final WordService wordService;
    private final ThemeRepository themeRepository;
    private final ProviderRepository providerRepository;
    private final ModelRepository modelRepository;
    private final ProviderModelRepository providerModelRepository;
    private final Logger log = LoggerFactory.getLogger(StandardCrosswordService.class);
    private final int iterations;
    private final int attemptsLimit;
    private final StandardCrosswordGeneratorRegistry registry;

    public StandardCrosswordService(
            ClueRepository clueRepository,
            LanguageRepository languageRepository,
            StandardCrosswordRepository crosswordRepository,
            ThemeRepository themeRepository,
            WordService wordService,
            ProviderModelRepository providerModelRepository,
            ProviderRepository providerRepository,
            ModelRepository modelRepository,
            @Value("${crossword-service.iterations}") int iterations,
            @Value("${crossword-service.attempts-limit}") int attemptsLimit,
            StandardCrosswordGeneratorRegistry registry
    ) {
        this.clueRepository = clueRepository;
        this.languageRepository = languageRepository;
        this.crosswordRepository = crosswordRepository;
        this.themeRepository = themeRepository;
        this.wordService = wordService;
        this.providerModelRepository = providerModelRepository;
        this.providerRepository = providerRepository;
        this.modelRepository = modelRepository;
        this.iterations = iterations;
        this.attemptsLimit = attemptsLimit;
        this.registry = registry;
    }

    public GetCrosswordsResponseDTO getCrosswords(@NotNull GetCrosswordsRequestDTO request) {
        log.info("Fetching crosswords...");
        Specification<StandardCrossword> specification = Specification.where(null);
        try {
            if (request.creator() != null) {
                specification = specification.and(StandardCrosswordUtil.getCreatorSpecification(request.creator()));
            }
            if (request.language() != null) {
                Language language = LanguageUtil.findLanguageByName(request.language(), languageRepository);
                specification = specification.and(StandardCrosswordUtil.getLanguageSpecification(language));
            }
            if (request.provider() != null) {
                Provider provider = ProviderUtil.findProvider(request.provider(), providerRepository);
                specification = specification.and(StandardCrosswordUtil.getProviderSpecification(provider));
            }
            if (request.model() != null) {
                Model model = ModelUtil.findModel(request.model(), modelRepository);
                specification = specification.and(StandardCrosswordUtil.getModelSpecification(model));
            }
            if (request.height() != null) {
                specification = specification.and(StandardCrosswordUtil.getHeightSpecification(request.height()));
            }
            if (request.width() != null) {
                specification = specification.and(StandardCrosswordUtil.getWidthSpecification(request.width()));
            }
            Page<StandardCrossword> crosswords = crosswordRepository.findAll(specification, PageRequest.of(request.page(), request.quantity()));
            List<StandardCrosswordResponse> crosswordDTOS = crosswords.get()
                    .map(crossword -> StandardCrosswordResponseMapperFactory.get(crossword.getType()).map(crossword))
                    .toList();
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

    public StandardCrosswordResponse generateWithoutAi(@NotNull GenerateStandardCrosswordRequest request) {
        Language language = LanguageUtil.findLanguageByName(request.language(), languageRepository);
        Theme theme = ThemeUtil.findThemeByName(request.theme(), themeRepository);
        List<Clue> clues = clueRepository.findByThemeAndLanguageAndNullProviderModelFetchedWord(theme, language, Math.max(request.height(), request.width()));
        if (clues.isEmpty()) {
            throw new ServerException(String.format("Words not found for theme %s", request.theme()));
        }
        List<String> words = clues.stream().map(clue -> clue.getWord().getText()).toList();
        var generatedCrossword = StandardCrosswordHelper.generateCrossword(words, request.height(), request.width(), iterations, registry.get(request.type()));
        Map<String, Clue> clueMap = ClueHelper.selectCluesByCrosswordWords(clues, words);
        StandardCrossword crosswordModel = StandardCrosswordHelper.createCrosswordModel(
                generatedCrossword,
                theme,
                language,
                null,
                request.type(),
                request.height(),
                request.width(),
                clueMap,
                UserUtil.getUserIdBySecurityContext().orElse(null)
        );
        StandardCrossword crossword = crosswordRepository.save(crosswordModel);
        StandardCrosswordResponseMapper crosswordDTOBuilder = StandardCrosswordResponseMapperFactory.get(crossword.getType());
        return crosswordDTOBuilder.map(crossword);
    }

    public StandardCrosswordResponse generateWithAi(@NotNull GenerateStandardCrosswordRequest request) {
        Theme theme = ThemeUtil.findOrCreate(request.theme(), themeRepository);
        Language language = LanguageUtil.findOrCreate(request.language(), languageRepository);
        ProviderModel providerModel = findProviderModel(request.provider(), request.model(), providerRepository, modelRepository, providerModelRepository);
        assert providerModel != null;
        List<Clue> cluesFromDatabase = ClueHelper.getOrGenerate(
                theme,
                language,
                providerModel.getProvider(),
                providerModel.getModel(),
                Math.max(request.height(), request.width()),
                request.height() * request.width(),
                providerRepository,
                modelRepository,
                providerModelRepository,
                clueRepository,
                wordService,
                attemptsLimit
        );
        List<String> words = cluesFromDatabase.stream().map(clue -> clue.getWord().getText()).toList();
        var finalCrossword = StandardCrosswordHelper.generateCrossword(words, request.height(), request.width(), iterations, registry.get(request.type()));

        Map<String, Clue> clueMap = ClueHelper.selectCluesByCrosswordWords(cluesFromDatabase, finalCrossword.getWords().stream().map(word -> word.getContent()).toList());

        StandardCrossword crosswordModel = StandardCrosswordHelper.createCrosswordModel(
                finalCrossword,
                theme,
                language,
                providerModel,
                request.type(),
                request.height(),
                request.width(),
                clueMap,
                UserUtil.getUserIdBySecurityContext().orElse(null)
        );
        StandardCrossword crossword = crosswordRepository.save(crosswordModel);
        log.debug("Crossword generated with theme: {}, provider: {}, model: {}", request.theme(), request.provider(), request.model());
        StandardCrosswordResponseMapper crosswordDTOBuilder = StandardCrosswordResponseMapperFactory.get(crossword.getType());
        return crosswordDTOBuilder.map(crossword);
    }

    public Object generateCrossword(@NotNull GenerateStandardCrosswordRequest request) {
        boolean useAi = request.provider() != null || request.model() != null;
        if (useAi) {
            return generateWithAi(request);
        }
        return generateWithoutAi(request);
    }

    public StandardCrosswordResponse getCrosswordByUuid(@NotNull StandardCrosswordRequest request) {
        StandardCrossword crossword = crosswordRepository.findByUuidFetchedThemeAndCoordinatesAndClueAndWordAndLanguage(request.uuid())
                .orElseThrow(() -> new CrosswordNotFoundException("Crossword not found for the given uuid"));
        log.info("Found crossword with uuid: {}", crossword.getUuid());
        StandardCrosswordResponseMapper crosswordDTOBuilder = StandardCrosswordResponseMapperFactory.get(crossword.getType());
        return crosswordDTOBuilder.map(crossword);
    }
}
