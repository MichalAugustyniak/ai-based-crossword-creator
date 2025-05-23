package com.aibasedcrosswordcreator.aiservice.communication.factory;

import com.aibasedcrosswordcreator.aiservice.communication.exception.ChatFactoryException;
import com.aibasedcrosswordcreator.aiservice.communication.model.Chat;
import com.aibasedcrosswordcreator.aiservice.communication.model.Gpt35Turbo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatFactoryTest {

    @Test
    void shouldCreateGpt35Turbo() {
        // given
        String provider = "openai";
        String model = "gpt-3.5-turbo";

        // when
        Chat chat = ChatFactory.create(provider, model);

        // then
        assertInstanceOf(Gpt35Turbo.class, chat);
    }

    @Test
    void shouldThrowChatFactoryException() {
        // given
        String provider = "-";
        String model = "-";

        // when
        Throwable exception = assertThrows(ChatFactoryException.class, () -> ChatFactory.create(provider, model));

        // then
        assertEquals("No such provider or model", exception.getMessage());
    }
}