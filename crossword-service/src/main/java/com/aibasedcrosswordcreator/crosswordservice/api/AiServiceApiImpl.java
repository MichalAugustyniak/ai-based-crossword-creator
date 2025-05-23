package com.aibasedcrosswordcreator.crosswordservice.api;

import com.aibasedcrosswordcreator.crosswordservice.dto.AiRequest;
import com.aibasedcrosswordcreator.crosswordservice.dto.AiResponse;
import com.aibasedcrosswordcreator.crosswordservice.exception.AiException;
import com.aibasedcrosswordcreator.crosswordservice.keycloak.api.KeycloakApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AiServiceApiImpl implements AiServiceApi {
    private final WebClient.Builder webClientBuilder;

    @Override
    public AiResponse sendMessage(AiRequest request, String token) {
        AiResponse response = webClientBuilder.build()
                .post()
                .uri("http://ai-service/api/ai")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(AiResponse.class)
                .block();
        if (Optional.ofNullable(response).isEmpty()) {
            throw new AiException("No words generated");
        }
        return response;
    }
}
