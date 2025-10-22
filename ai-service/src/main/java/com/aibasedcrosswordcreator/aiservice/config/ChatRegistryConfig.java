package com.aibasedcrosswordcreator.aiservice.config;

import com.aibasedcrosswordcreator.aiservice.chat.OpenAiChat;
import com.aibasedcrosswordcreator.aiservice.registry.ChatRegistry;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
public class ChatRegistryConfig {
    @Bean
    public ChatRegistry chatRegistry() {
        return new ChatRegistry();
    }

    @Configuration
    @RefreshScope
    public static class RegisterOpenAiChats {
        ChatRegistry chatRegistry;

        @Value("${ENABLED_OPENAI_MODELS}")
        List<String> enabledOpenAiModels;

        @Value("${OPENAI_API_KEY:#{null}}")
        String key;

        public RegisterOpenAiChats(ChatRegistry chatRegistry) {
            this.chatRegistry = chatRegistry;
        }

        @PostConstruct
        @EventListener(value = RefreshScopeRefreshedEvent.class)
        public void registerOpenAiChats() {
            chatRegistry.unregisterProvider("openai");
            if (enabledOpenAiModels.isEmpty() || key == null || key.isEmpty()) {
                return;
            }
            var openAIClient = OpenAIOkHttpClient.builder()
                    .apiKey(key)
                    .build();
            enabledOpenAiModels.forEach(enabled -> chatRegistry.register("openai", enabled, new OpenAiChat(openAIClient, ChatModel.of(enabled))));
        }
    }
}
