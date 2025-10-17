package com.aibasedcrosswordcreator.crosswordservice.config;

import com.aibasedcrosswordcreator.crosswordservice.proxy.StandardCrosswordServiceProxy;
import com.aibasedcrosswordcreator.crosswordservice.registry.CrosswordServiceProxyRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrosswordServiceProxyRegistryConfig {
    @Bean
    public CrosswordServiceProxyRegistry crosswordServiceRegistry(StandardCrosswordServiceProxy standardCrosswordServiceProxy) {
        var registry = new CrosswordServiceProxyRegistry();
        registry.register("standard-crossword", standardCrosswordServiceProxy);
        return registry;
    }
}
