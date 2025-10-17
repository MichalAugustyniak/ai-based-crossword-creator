package com.aibasedcrosswordcreator.crosswordservice.helper;

import com.aibasedcrosswordcreator.crosswordservice.dto.GenerateWordsDetailsDTO;
import com.aibasedcrosswordcreator.crosswordservice.dto.GenerateWordsRequest;
import com.aibasedcrosswordcreator.crosswordservice.exception.ServerException;
import com.aibasedcrosswordcreator.crosswordservice.model.*;
import com.aibasedcrosswordcreator.crosswordservice.repository.*;
import com.aibasedcrosswordcreator.crosswordservice.service.WordService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.aibasedcrosswordcreator.crosswordservice.util.ProviderModelUtil.findProviderModel;

public class ClueHelper {
    public static List<Clue> getOrGenerate(
            Theme theme,
            Language language,
            Provider provider,
            Model model,
            int length,
            int quantity,
            ProviderRepository providerRepository,
            ModelRepository modelRepository,
            ProviderModelRepository providerModelRepository,
            ClueRepository clueRepository,
            WordService wordService,
            int attemptsLimit
    ) {
        int maxWordLength = length - 1;
        ProviderModel providerModel = findProviderModel(provider.getName(), model.getName(), providerRepository, modelRepository, providerModelRepository);
        List<Clue> cluesFromDatabase = clueRepository.findByThemeAndLanguageAndProviderModelFetchedWord(
                theme,
                language,
                providerModel,
                maxWordLength
        );
        int currentAttempt = 0;
        while (currentAttempt < attemptsLimit) {
            if (cluesFromDatabase.size() < quantity) {
                try {
                    wordService.generateWords(
                            new GenerateWordsRequest(
                                    provider.getName(),
                                    model.getName(),
                                    new GenerateWordsDetailsDTO(
                                            theme.getName(),
                                            language.getText(),
                                            quantity,
                                            maxWordLength
                                    )
                            )
                    );
                    cluesFromDatabase = clueRepository.findByThemeAndLanguageAndProviderModelFetchedWord(
                            theme,
                            language,
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
            } else {
                break;
            }
        }
        return cluesFromDatabase;
    }

    public static Map<String, Clue> selectCluesByCrosswordWords(List<Clue> clues, List<String> words) {
        assert clues.size() >= words.size();
        final Map<String, Clue> clueMap = new HashMap<>();
        for (var word : words) {
            clueMap.put(word, null);
        }
        for (var clue : clues) {
            if (clueMap.containsKey(clue.getWord().getText())) {
                clueMap.put(clue.getWord().getText(), clue);
            }
        }
        for (var entry : clueMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new ServerException(String.format("Exception occurred while collecting used crossword words: Missing clue for word '%s'", entry.getKey()));
            }
        }
        return clueMap;
    }
}
