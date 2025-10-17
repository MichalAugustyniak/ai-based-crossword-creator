package com.aibasedcrosswordcreator.crosswordservice.mapper;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class StandardCrosswordResponseMapperFactory {
    private static final Map<String, StandardCrosswordResponseMapper> crosswordResponseBuilderMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(StandardCrosswordResponseMapper.class);
        var subTypes = reflections.getSubTypesOf(StandardCrosswordResponseMapper.class);
        for (var type : subTypes) {
            try {
                StandardCrosswordResponseMapperFactory.crosswordResponseBuilderMap.put(
                        type.getDeclaredConstructor().newInstance().type(),
                        type.getDeclaredConstructor().newInstance());
            } catch (Exception ignored) {

            }
        }
    }

    public static StandardCrosswordResponseMapper get(String type) {
        StandardCrosswordResponseMapper crosswordResponseBuilder = StandardCrosswordResponseMapperFactory.crosswordResponseBuilderMap.get(type);
        return crosswordResponseBuilder.newInstance();
    }
}
