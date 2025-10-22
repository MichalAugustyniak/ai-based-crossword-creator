package com.aibasedcrosswordcreator.aiservice.service;

import com.aibasedcrosswordcreator.aiservice.chat.Chat;
import com.aibasedcrosswordcreator.aiservice.dto.AiMessageRequest;
import com.aibasedcrosswordcreator.aiservice.dto.AiMessageResponse;
import com.aibasedcrosswordcreator.aiservice.registry.ChatRegistry;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AiService {
    private final ChatRegistry registry;

    public AiMessageResponse message(@NotNull AiMessageRequest request) {
        Chat chat = registry.get(request.provider(), request.model());
        String message = chat.message(request.message());
        return new AiMessageResponse(message, request.provider(), request.model());
    }
}
