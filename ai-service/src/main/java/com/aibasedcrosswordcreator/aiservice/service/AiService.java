package com.aibasedcrosswordcreator.aiservice.service;

import com.aibasedcrosswordcreator.aiservice.communication.manager.ChatManager;
import com.aibasedcrosswordcreator.aiservice.communication.model.Chat;
import com.aibasedcrosswordcreator.aiservice.dto.AiDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AiService {
    private final ChatManager chatManager;

    public AiDTO ai(@NotNull AiDTO aiDTO) {
        //Chat chat = ChatFactory.create(aiDTO.provider().orElse("gpt-3.5-turbo"), aiDTO.model().orElse("openai"));
        //chat.setKey(key);
        Chat chat = chatManager.getChat(aiDTO.provider(), aiDTO.model());
        String message = chat.message(aiDTO.message());
        return new AiDTO(message, aiDTO.provider(), aiDTO.model());
    }
}
