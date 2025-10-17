package com.aibasedcrosswordcreator.crosswordservice.config;

import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGeneratorV1;
import com.aibasedcrosswordcreator.crosswordservice.generator.CrosswordGeneratorV2;
import com.aibasedcrosswordcreator.crosswordservice.registry.StandardCrosswordGeneratorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StandardCrosswordGeneratorRegistryConfig {
    @Bean
    public StandardCrosswordGeneratorRegistry standardCrosswordGeneratorRegistry() {
        var registry = new StandardCrosswordGeneratorRegistry();
        registry.register("V1", new CrosswordGeneratorV1());
        registry.register("V2", new CrosswordGeneratorV2());
        return registry;
    }
}
