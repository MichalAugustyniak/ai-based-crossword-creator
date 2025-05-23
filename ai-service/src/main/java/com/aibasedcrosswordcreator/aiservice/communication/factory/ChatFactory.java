package com.aibasedcrosswordcreator.aiservice.communication.factory;

import com.aibasedcrosswordcreator.aiservice.communication.exception.ChatFactoryException;
import com.aibasedcrosswordcreator.aiservice.communication.model.Chat;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChatFactory {

    private static final Map<String, Chat> chatMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(Chat.class);
        var subTypes = reflections.getSubTypesOf(Chat.class);

        for (var type : subTypes) {
            try {
                ChatFactory.chatMap.put(
                        type.getDeclaredConstructor().newInstance().getProvider() + "|" +
                        type.getDeclaredConstructor().newInstance().getName(),
                        type.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Chat create(String provider, String model) {
        Chat chat = ChatFactory.chatMap.get(provider + "|" + model);
        if (Optional.ofNullable(chat).isEmpty()) {
            throw new ChatFactoryException("No such provider or model");
        }

        return chat.newInstance();
    }
}
