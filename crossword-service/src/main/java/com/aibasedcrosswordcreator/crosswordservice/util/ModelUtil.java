package com.aibasedcrosswordcreator.crosswordservice.util;

import com.aibasedcrosswordcreator.crosswordservice.exception.ModelNotFoundException;
import com.aibasedcrosswordcreator.crosswordservice.model.Model;
import com.aibasedcrosswordcreator.crosswordservice.repository.ModelRepository;

public class ModelUtil {
    public static Model findModel(String name, ModelRepository repository) {
        return repository.findByName(name)
                .orElseThrow(() -> new ModelNotFoundException(String.format("Model '%s' not found", name)));
    }
}
