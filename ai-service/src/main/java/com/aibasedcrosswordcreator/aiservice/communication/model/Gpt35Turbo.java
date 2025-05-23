package com.aibasedcrosswordcreator.aiservice.communication.model;

import com.aibasedcrosswordcreator.aiservice.communication.exception.AiException;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

public class Gpt35Turbo implements Chat {
    public static final String MODEL = "gpt-3.5-turbo";
    public static final String PROVIDER = "openai";
    private final String url;
    private final String key;

    public Gpt35Turbo(
            String url,
            String key
    ) {
        this.url = url;
        this.key = key;
    }

    @Override
    public String message(String message) {
        Gtp35TurboRequest request = new Gtp35TurboRequest(MODEL, message);
        WebClient webClient = WebClient.create(url);
        Gpt35TurboResponse response = webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                .bodyValue(request)
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(), httpResponse -> {
                    throw new AiException("Something went wrong while sending or getting the message.");
                })
                .bodyToMono(Gpt35TurboResponse.class)
                .block();
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "";
        }
        return response.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public Chat newInstance() {
        return new Gpt35Turbo(url, key);
    }

    @Override
    public String getName() {
        return MODEL;
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }

    @Getter
    @Setter
    public static class Message {
        private String role;
        private String content;

        public Message(String user, String prompt) {
            this.role = user;
            this.content = prompt;
        }
    }

    @Getter
    @Setter
    public static class Gtp35TurboRequest {
        private String model;
        private List<Message> messages;

        public Gtp35TurboRequest(String model, String prompt) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("user", prompt));
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Gpt35TurboResponse {
        private List<Choice> choices;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Choice {
            private int index;
            private Message message;

            public Choice(Message message) {
                this.message = message;
            }
        }
    }
}
