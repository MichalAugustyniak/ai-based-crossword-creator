package com.aibasedcrosswordcreator.crosswordservice.util;

import com.aibasedcrosswordcreator.crosswordservice.exception.ThemeNotFoundException;
import com.aibasedcrosswordcreator.crosswordservice.model.Language;
import com.aibasedcrosswordcreator.crosswordservice.repository.LanguageRepository;

public class LanguageUtil {
    public static Language findLanguageByName(String name, LanguageRepository repository) {
        return repository.findByText(name)
                .orElseThrow(() -> new ThemeNotFoundException(String.format("Language '%s' not found.", name)));
    }

    public static Language findOrCreate(String name, LanguageRepository repository) {
        return repository.findByText(name)
                .orElseGet(() -> {
                    Language language = new Language();
                    language.setText(name);
                    return repository.save(language);
                });
    }
}
