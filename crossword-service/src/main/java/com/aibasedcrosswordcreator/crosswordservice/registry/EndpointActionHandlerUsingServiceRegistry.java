package com.aibasedcrosswordcreator.crosswordservice.registry;

import com.aibasedcrosswordcreator.crosswordservice.exception.RegistryException;
import com.aibasedcrosswordcreator.crosswordservice.handler.EndpointActionHandlerUsingService;

import java.util.HashMap;
import java.util.Optional;

public class EndpointActionHandlerUsingServiceRegistry<S> {
    private final HashMap<String, EndpointActionHandlerUsingService<S>> handlers = new HashMap<>();

    public void register(String key, EndpointActionHandlerUsingService<S> handler) {
        handlers.put(key, handler);
    }

    public EndpointActionHandlerUsingService<S> getHandler(String key) {
        if (!handlers.containsKey(key)) {
            throw new RegistryException(String.format("Handler not found for key '%s'.", key));
        }
        return Optional.ofNullable(handlers.get(key))
                .orElseThrow(() -> new RegistryException(String.format("Handler is null for key '%s'.", key)));
    }
}
