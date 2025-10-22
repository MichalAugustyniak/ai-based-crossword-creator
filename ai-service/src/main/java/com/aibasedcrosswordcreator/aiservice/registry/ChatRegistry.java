package com.aibasedcrosswordcreator.aiservice.registry;

import com.aibasedcrosswordcreator.aiservice.chat.Chat;
import com.aibasedcrosswordcreator.aiservice.exception.RegistryException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class ChatRegistry {
    private final Map<String, Map<String, Chat>> chatMap = new HashMap<>();

    public void register(String provider, String model, Chat chat) {
        var modelMap = Optional.ofNullable(chatMap.get(provider))
                .orElseGet(() -> {
                    var map = new HashMap<String, Chat>();
                    chatMap.put(provider, map);
                    return map;
                });
        if (modelMap.containsKey(model)) {
            throw new RegistryException(String.format("Chat of model name '%s' is already registered.", model));
        }
        modelMap.put(model, chat);
    }

    public Chat get(String provider, String model) {
        var modelMap = chatMap.get(provider);
        if (modelMap == null) {
            throw new RegistryException(String.format("Chat of provider name '%s' not found.", provider));
        }
        return Optional.ofNullable(modelMap.get(model))
                .orElseThrow(() -> new RegistryException(String.format("Chat of model name '%s' not found.", model)));
    }

    public void unregisterProvider(String provider) {
        chatMap.remove(provider);
    }
}
