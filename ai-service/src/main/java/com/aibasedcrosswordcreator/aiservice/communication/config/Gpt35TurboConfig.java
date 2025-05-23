package com.aibasedcrosswordcreator.aiservice.communication.config;

import com.aibasedcrosswordcreator.aiservice.communication.manager.ChatManager;
import com.aibasedcrosswordcreator.aiservice.communication.model.Gpt35Turbo;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
public class Gpt35TurboConfig {
    @Value("${gpt35turbo.url:null}")
    private String url;

    @Value("${gpt35turbo.key:null}")
    private String key;

    private final ChatManager chatManager;

    @PostConstruct
    public void chatManager() {
        if (url == null || key == null) {

            return;
        }
        Gpt35Turbo gpt35Turbo = new Gpt35Turbo(url, key);
        chatManager.putChat(gpt35Turbo.getProvider(), gpt35Turbo.getName(), gpt35Turbo);
    }
}
