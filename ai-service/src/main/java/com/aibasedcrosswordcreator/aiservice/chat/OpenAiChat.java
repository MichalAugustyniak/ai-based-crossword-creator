package com.aibasedcrosswordcreator.aiservice.chat;

import com.openai.client.OpenAIClient;
import com.openai.errors.RateLimitException;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenAiChat implements Chat {
    private final OpenAIClient client;
    private final ChatModel model;

    @Override
    public String message(String message) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(message)
                .model(model)
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        try {
            return chatCompletion.choices().get(0).message().toString();
        } catch (RateLimitException e) {
            throw new com.aibasedcrosswordcreator.aiservice.exception.RateLimitException("OpenAI api quota exceeded.");
        }
    }
}
