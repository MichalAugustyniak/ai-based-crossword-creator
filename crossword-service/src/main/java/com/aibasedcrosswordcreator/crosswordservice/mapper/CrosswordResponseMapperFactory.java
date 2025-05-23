package com.aibasedcrosswordcreator.crosswordservice.mapper;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class CrosswordResponseMapperFactory {
    private static final Map<String, CrosswordResponseMapper> crosswordResponseBuilderMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(CrosswordResponseMapper.class);
        var subTypes = reflections.getSubTypesOf(CrosswordResponseMapper.class);
        for (var type : subTypes) {
            try {
                CrosswordResponseMapperFactory.crosswordResponseBuilderMap.put(
                        type.getDeclaredConstructor().newInstance().type(),
                        type.getDeclaredConstructor().newInstance());
            } catch (Exception ignored) {

            }
        }
    }

    public static CrosswordResponseMapper create(String type) {
        CrosswordResponseMapper crosswordResponseBuilder = CrosswordResponseMapperFactory.crosswordResponseBuilderMap.get(type);
        return crosswordResponseBuilder.newInstance();
    }
}
