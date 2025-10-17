package com.aibasedcrosswordcreator.crosswordservice.config;

import com.aibasedcrosswordcreator.crosswordservice.handler.GenerateCrosswordActionHandler;
import com.aibasedcrosswordcreator.crosswordservice.registry.EndpointActionHandlerUsingServiceRegistry;
import com.aibasedcrosswordcreator.crosswordservice.proxy.CrosswordServiceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrosswordEndpointActionHandlerUsingServiceRegistryConfig {
    @Bean
    public EndpointActionHandlerUsingServiceRegistry<CrosswordServiceProxy> endpointActionHandlerUsingServiceProxyRegistry() {
        var registry = new EndpointActionHandlerUsingServiceRegistry<CrosswordServiceProxy>();
        registry.register("generate", new GenerateCrosswordActionHandler());
        return registry;
    }
}
