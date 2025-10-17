package com.aibasedcrosswordcreator.crosswordservice.registry;

import com.aibasedcrosswordcreator.crosswordservice.exception.RegistryException;
import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGenerator;

import java.util.HashMap;
import java.util.Optional;

public class StandardCrosswordGeneratorRegistry {
    private final HashMap<String, CrosswordGenerator> generators = new HashMap<>();

    public void register(String key, CrosswordGenerator generator) {
        if (generators.containsKey(key)) {
            throw new RegistryException(String.format("StandardCrosswordGenerator for key '%s' is already registered.", key));
        }
        generators.put(key, generator);
    }

    public CrosswordGenerator get(String key) {
        return Optional.ofNullable(generators.get(key))
                .orElseThrow(() -> new RegistryException(String.format("StandardCrosswordGenerator not found for key '%s'.", key)));
    }
}
