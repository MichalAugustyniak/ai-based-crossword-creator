package com.aibasedcrosswordcreator.aiservice.communication.manager;

import com.aibasedcrosswordcreator.aiservice.communication.exception.ModelNotFoundException;
import com.aibasedcrosswordcreator.aiservice.communication.exception.ProviderNotFoundException;
import com.aibasedcrosswordcreator.aiservice.communication.model.Chat;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ChatManager {
    private final Map<String, Map<String, Chat>> chats = new HashMap<>();

    public Chat getChat(String provider, String model) {
        if (!chats.containsKey(provider)) {
            throw new ProviderNotFoundException(String.format("Provider '%s' not found.", provider));
        }
        Map<String, Chat> m = chats.get(provider);
        Optional.ofNullable(m)
                .orElseThrow(() -> new ModelNotFoundException(String.format("Model '%s' not found.", model)));
        if (!m.containsKey(model)) {
            throw new ModelNotFoundException(String.format("Model '%s' not found.", model));
        }
        return Optional.ofNullable(m.get(model))
                .orElseThrow(() -> new ModelNotFoundException(String.format("Model '%s' not found.", model)));
    }

    public void putChat(String provider, String model, Chat chat) {
        if (chats.get(provider) == null) {
            Map<String, Chat> providerModel = new HashMap<>();
            providerModel.put(model, chat);
            chats.put(provider, providerModel);
        }
        Map<String, Chat> models = chats.get(provider);
        models.put(model, chat);
    }
}
