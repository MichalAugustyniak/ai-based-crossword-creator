package com.aibasedcrosswordcreator.crosswordservice.config;

import com.aibasedcrosswordcreator.crosswordservice.handler.GenerateWordsEndpointActionHandler;
import com.aibasedcrosswordcreator.crosswordservice.handler.SaveWordsGenerateCluesEndpointActionHandler;
import com.aibasedcrosswordcreator.crosswordservice.handler.SaveWordsWithCluesEndpointActionHandler;
import com.aibasedcrosswordcreator.crosswordservice.registry.EndpointActionHandlerUsingServiceRegistry;
import com.aibasedcrosswordcreator.crosswordservice.service.WordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WordEndpointActionHandlerUsingServiceRegistryConfig {
    @Bean
    public EndpointActionHandlerUsingServiceRegistry<WordService> endpointActionHandlerUsingService() {
        var registry = new EndpointActionHandlerUsingServiceRegistry<WordService>();
        registry.register("save-words-with-clues", new SaveWordsWithCluesEndpointActionHandler());
        registry.register("save-words-generate-clues", new SaveWordsGenerateCluesEndpointActionHandler());
        registry.register("generate-words", new GenerateWordsEndpointActionHandler());
        return registry;
    }
}
