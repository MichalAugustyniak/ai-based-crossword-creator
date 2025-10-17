package com.aibasedcrosswordcreator.crosswordservice.util;

import com.aibasedcrosswordcreator.crosswordservice.exception.ThemeNotFoundException;
import com.aibasedcrosswordcreator.crosswordservice.model.Theme;
import com.aibasedcrosswordcreator.crosswordservice.repository.ThemeRepository;

public class ThemeUtil {
    public static Theme findThemeByName(String name, ThemeRepository repository) {
        return repository.findByThemeName(name)
                .orElseThrow(() -> new ThemeNotFoundException(String.format("Theme '%s' not found", name)));
    }

    public static Theme findOrCreate(String name, ThemeRepository repository) {
        return repository.findByThemeName(name)
                .orElseGet(() -> {
                    Theme theme = new Theme();
                    theme.setName(name);
                    return repository.save(theme);
                });
    }
}
