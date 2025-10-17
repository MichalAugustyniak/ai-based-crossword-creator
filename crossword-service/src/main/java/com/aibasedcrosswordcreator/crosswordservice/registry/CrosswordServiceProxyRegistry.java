package com.aibasedcrosswordcreator.crosswordservice.registry;

import com.aibasedcrosswordcreator.crosswordservice.exception.RegistryException;
import com.aibasedcrosswordcreator.crosswordservice.proxy.CrosswordServiceProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CrosswordServiceProxyRegistry {
    private final Map<String, CrosswordServiceProxy> proxies = new HashMap<>();

    public void register(String key, CrosswordServiceProxy crosswordService) {
        if (proxies.containsKey(key)) {
            throw new RegistryException(String.format("CrosswordService for key '%s' is already registered.", key));
        }
        proxies.put(key, crosswordService);
    }

    public CrosswordServiceProxy getCrosswordServiceProxy(String key) {
        return Optional.ofNullable(proxies.get(key))
                .orElseThrow(() -> new RegistryException(String.format("CrosswordService not found for key '%s'.", key)));
    }

    public CrosswordServiceProxy getAny() {
        for (var entry : proxies.entrySet()) {
            return entry.getValue();
        }
        throw new RegistryException("Empty CrosswordServiceProxyRegistry.");
    }
}
