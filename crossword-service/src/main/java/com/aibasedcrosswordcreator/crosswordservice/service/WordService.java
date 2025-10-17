package com.aibasedcrosswordcreator.crosswordservice.service;

import com.aibasedcrosswordcreator.crosswordservice.api.AiServiceApi;
import com.aibasedcrosswordcreator.crosswordservice.dto.*;
import com.aibasedcrosswordcreator.crosswordservice.exception.AiException;
import com.aibasedcrosswordcreator.crosswordservice.helper.WordHelper;
import com.aibasedcrosswordcreator.crosswordservice.keycloak.api.KeycloakApi;
import com.aibasedcrosswordcreator.crosswordservice.keycloak.dto.Token;
import com.aibasedcrosswordcreator.crosswordservice.model.*;
import com.aibasedcrosswordcreator.crosswordservice.prompt.WordPromptBuilder;
import com.aibasedcrosswordcreator.crosswordservice.prompt.WordPromptParser;
import com.aibasedcrosswordcreator.crosswordservice.repository.*;
import com.aibasedcrosswordcreator.crosswordservice.util.ProviderModelUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Validated
public class WordService {
    private final WordRepository wordRepository;
    private final ThemeRepository themeRepository;
    private final LanguageRepository languageRepository;
    private final ProviderModelRepository providerModelRepository;
    private final ProviderRepository providerRepository;
    private final ModelRepository modelRepository;
    private final ClueRepository clueRepository;
    private final AiServiceApi aiServiceApi;
    private final WordPromptBuilder wordPromptBuilder;
    private final int attemptsLimit;
    private final KeycloakApi keycloakApi;
    private final String adminUsername;
    private final String adminPassword;

    public WordService(
            WordRepository wordRepository,
            ThemeRepository themeRepository,
            LanguageRepository languageRepository,
            ProviderModelRepository providerModelRepository,
            ProviderRepository providerRepository,
            ModelRepository modelRepository,
            ClueRepository clueRepository,
            AiServiceApi aiServiceApi,
            WordPromptBuilder wordPromptBuilder,
            @Value("${crossword-service.attempts-limit}") int attemptsLimit,
            KeycloakApi keycloakApi,
            @Value("${keycloak.admin.username}") String adminUsername,
            @Value("${keycloak.admin.password}") String adminPassword
    ) {
        this.wordRepository = wordRepository;
        this.themeRepository = themeRepository;
        this.languageRepository = languageRepository;
        this.providerModelRepository = providerModelRepository;
        this.providerRepository = providerRepository;
        this.modelRepository = modelRepository;
        this.clueRepository = clueRepository;
        this.aiServiceApi = aiServiceApi;
        this.wordPromptBuilder = wordPromptBuilder;
        this.attemptsLimit = attemptsLimit;
        this.keycloakApi = keycloakApi;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public WordsResponse generateWords(@NotNull GenerateWordsRequest generateWordsRequestDTO) {
        Token token = keycloakApi.getToken(adminUsername, adminPassword, "password");
        String prompt = wordPromptBuilder.generateWords(generateWordsRequestDTO.details());
        AiRequest body = new AiRequest(prompt, generateWordsRequestDTO.provider(), generateWordsRequestDTO.model());
        List<String> words = null;
        int currentAttempt = 0;
        while (currentAttempt < attemptsLimit) {
            try {
                AiResponse response = aiServiceApi.sendMessage(body, token.getAccessToken());
                words = wordPromptBuilder.getParser().parseGeneratedWordsToList(response.message());
                break;
            } catch (Exception e) {
                long delay = (long) Math.pow(2, ++currentAttempt);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        Optional.ofNullable(words)
                .orElseThrow(() -> new AiException("Something went wrong while generating words"));
        Language language = languageRepository.findByText(generateWordsRequestDTO.details().language())
                .orElseGet(() -> {
                    Language newLanguage = new Language();
                    newLanguage.setText(generateWordsRequestDTO.details().language());
                    return languageRepository.save(newLanguage);
                });
        Theme theme = themeRepository.findByName(generateWordsRequestDTO.details().theme())
                .orElseGet(() -> {
                    Theme newTheme = new Theme();
                    newTheme.setName(generateWordsRequestDTO.details().theme());
                    return themeRepository.save(newTheme);
                });
        List<Word> existingWords = wordRepository.findAllByTextIsIn(words);
        Set<String> wordSet = new HashSet<>(words);
        String providerName = generateWordsRequestDTO.provider();
        String modelName = generateWordsRequestDTO.model();
        ProviderModel providerModel = ProviderModelUtil.findProviderModel(
                providerName,
                modelName,
                providerRepository,
                modelRepository,
                providerModelRepository
        );
        for (Word word : existingWords) {
            wordSet.removeIf(w -> w.equals(word.getText()));
        }
        String[] filteredWords = wordSet.toArray(new String[0]);
        prompt = wordPromptBuilder.generateClues(new SaveWordsAndGenerateCluesRequest(
                language.getText(),
                theme.getName(),
                generateWordsRequestDTO.provider(),
                generateWordsRequestDTO.model(),
                Arrays.asList(filteredWords)
        ));
        List<String> clues = null;
        WordPromptParser parser = wordPromptBuilder.getParser();
        currentAttempt = 0;
        while (currentAttempt < attemptsLimit) {
            try {
                String response = aiServiceApi.sendMessage(new AiRequest(
                        generateWordsRequestDTO.provider(),
                        generateWordsRequestDTO.model(),
                        prompt + Arrays.asList(filteredWords)
                ), token.getAccessToken()).message();
                clues = parser.parseGeneratedCluesToList(response);
                break;
            } catch (Exception e) {
                long delay = (long) Math.pow(2, ++currentAttempt);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        Optional.ofNullable(clues)
                .orElseThrow(() -> new AiException("Something went wrong while generating clues"));
        currentAttempt = 0;
        while (clues.size() < filteredWords.length) {
            try {
                String[] wordsLeft = Arrays.copyOfRange(filteredWords, clues.size(), filteredWords.length);
                String nextClues = aiServiceApi.sendMessage(new AiRequest(
                        generateWordsRequestDTO.provider(),
                        generateWordsRequestDTO.model(),
                        prompt + Arrays.asList(wordsLeft)
                ), token.getAccessToken()).message();
                clues.addAll(parser.parseGeneratedCluesToList(nextClues));
                currentAttempt = 0;
            } catch (Exception e) {
                long delay = (long) Math.pow(2, ++currentAttempt);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        Word[] builtWords = WordHelper.buildWords(filteredWords, clues.toArray(new String[0]), language, theme, providerModel);
        wordRepository.saveAll(Arrays.asList(builtWords));
        List<String> finalClues = clues;
        return new WordsResponse(
                IntStream.of(Math.min(builtWords.length, clues.size()))
                        .mapToObj(i -> new WordAndClueDTO(builtWords[i].getText(), finalClues.get(i)))
                        .toList()
        );
    }

    public void saveWordsAndGenerateClues(@NotNull SaveWordsAndGenerateCluesRequest request) {
        Token token = keycloakApi.getToken(adminUsername, adminPassword, "client_credentials");
        String query = wordPromptBuilder.generateClues(request);
        WordPromptParser parser = wordPromptBuilder.getParser();
        String providerName = request.provider();
        String modelName = request.model();
        Language language = languageRepository.findByText(request.language())
                .orElseGet(() -> {
                    Language newLanguage = new Language();
                    newLanguage.setText(request.language());
                    languageRepository.save(newLanguage);
                    return newLanguage;
                });
        Theme theme = themeRepository.findByThemeName(request.theme())
                .orElseGet(() -> {
                    Theme newTheme = new Theme();
                    newTheme.setName(request.theme());
                    themeRepository.save(newTheme);
                    return newTheme;
                });
        ProviderModel providerModel = ProviderModelUtil.findProviderModel(providerName, modelName, providerRepository, modelRepository, providerModelRepository);
        String[] wordList = request.words().toArray(new String[0]);
        List<String> clues = null;
        int currentAttempt = 0;
        while (currentAttempt < attemptsLimit) {
            try {
                AiResponse response = aiServiceApi.sendMessage(new AiRequest(providerName, modelName, query + List.of(request.words())), token.getAccessToken());
                clues = parser.parseGeneratedCluesToList(response.message());
                break;
            } catch (Exception e) {
                long delay = (long) Math.pow(2, ++currentAttempt);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        Optional.ofNullable(clues)
                .orElseThrow(() -> new AiException("Something went wrong while generating clues"));
        currentAttempt = 0;
        while (clues.size() < request.words().size()) {
            try {
                String[] wordsLeft = Arrays.copyOfRange(wordList, clues.size(), wordList.length);
                AiResponse response = aiServiceApi.sendMessage(new AiRequest(providerName, modelName, query + Arrays.asList(wordsLeft)), token.getAccessToken());
                List<String> nextClues = parser.parseGeneratedCluesToList(response.message());
                clues.addAll(nextClues);
            } catch (Exception e) {
                long delay = (long) Math.pow(2, ++currentAttempt);
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        Word[] words = WordHelper.buildWords(wordList, clues.toArray(new String[0]), language, theme, providerModel);
        wordRepository.saveAll(Arrays.asList(words));
    }

    public void addWordsWithClues(@NotNull SaveWordsWithCluesRequest request) {
        request = new SaveWordsWithCluesRequest(
                request.theme(),
                request.language(),
                request.provider(),
                request.model(),
                new ArrayList<>(request.wordsAndClues().stream().map(w -> new WordAndClueDTO(w.word().toLowerCase(), w.clue())).toList())
        );
        SaveWordsWithCluesRequest finalDto = request;
        Language language = languageRepository.findByText(request.language())
                .orElseGet(() -> {
                    Language newLanguage = new Language();
                    newLanguage.setText(finalDto.language());
                    return languageRepository.save(newLanguage);
                });
        Theme theme = themeRepository.findByName(request.theme())
                .orElseGet(() -> {
                    Theme newTheme = new Theme();
                    newTheme.setName(finalDto.theme());
                    return themeRepository.save(newTheme);
                });
        Provider provider = request.provider() != null ? providerRepository.findByName(request.provider())
                .orElseGet(() -> {
                    Provider newProvider = new Provider();
                    newProvider.setName(finalDto.provider());
                    newProvider.setDescription(null);
                    return providerRepository.save(newProvider);
                }) : null;
        Model model = request.model() != null ? modelRepository.findByName(request.model())
                .orElseGet(() -> {
                    Model newModel = new Model();
                    newModel.setName(finalDto.model());
                    newModel.setDescription(null);
                    return modelRepository.save(newModel);
                }) : null;
        ProviderModel providerModel = provider != null && model != null ? providerModelRepository.findByProviderAndModel(provider, model)
                .orElseGet(() -> {
                    ProviderModel newProviderModel = new ProviderModel();
                    newProviderModel.setProvider(provider);
                    newProviderModel.setModel(model);
                    return providerModelRepository.save(newProviderModel);
                }) : null;
        Set<String> wordStrings = finalDto.wordsAndClues()
                .stream()
                .map(WordAndClueDTO::word)
                .collect(Collectors.toSet());
        List<Clue> clues = clueRepository.findAllByTextCollectionAndThemeAndLanguageAndProviderModelFetchedWord(wordStrings, theme, language, providerModel);
        Map<String, Word> stringWordMap = new HashMap<>();
        List<String> words = finalDto.wordsAndClues().stream().map(WordAndClueDTO::word).toList();
        words.forEach(w -> stringWordMap.put(w, Word.builder().text(w).build()));
        wordRepository.findAllByTextIsIn(words).forEach((w) -> stringWordMap.put(w.getText(), w));
        Map<String, Word> alreadySaved = new HashMap<>();
        clues.forEach(c -> alreadySaved.put(c.getWord().getText(), c.getWord()));
        finalDto.wordsAndClues().removeIf(w -> alreadySaved.containsKey(w.word()));
        List<Clue> cluesToSave = finalDto.wordsAndClues()
                .stream()
                .map(w -> Clue.builder()
                        .text(w.clue())
                        .word(stringWordMap.get(w.word()))
                        .language(language)
                        .theme(theme)
                        .providerModel(providerModel)
                        .build())
                .toList();
        clueRepository.saveAll(cluesToSave);
    }
}
