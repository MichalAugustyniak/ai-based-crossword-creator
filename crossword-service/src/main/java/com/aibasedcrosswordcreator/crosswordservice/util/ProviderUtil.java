package com.aibasedcrosswordcreator.crosswordservice.util;

import com.aibasedcrosswordcreator.crosswordservice.exception.ProviderNotFoundException;
import com.aibasedcrosswordcreator.crosswordservice.model.Provider;
import com.aibasedcrosswordcreator.crosswordservice.repository.ProviderRepository;

public class ProviderUtil {
    public static Provider findProvider(String name, ProviderRepository repository) {
        return repository.findByName(name)
                .orElseThrow(() -> new ProviderNotFoundException(String.format("Provider '%s' not found.", name)));
    }
}
