package com.aibasedcrosswordcreator.authservice.api;

import com.aibasedcrosswordcreator.authservice.dto.SetPropertyRequest;
import com.aibasedcrosswordcreator.authservice.exception.ApiClientException;
import com.aibasedcrosswordcreator.authservice.exception.ApiServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ConfigPropertyApiImpl implements ConfigPropertyApi {
    private final WebClient.Builder builder;
    private final String applicationName;
    private final String applicationProfile;

    @Autowired
    public ConfigPropertyApiImpl(
            WebClient.Builder builder,
            @Value("${spring.application.name}") String applicationName,
            @Value("${spring.profiles.active}") String applicationProfile
    ) {
        this.builder = builder;
        this.applicationName = applicationName;
        this.applicationProfile = applicationProfile;
    }

    @Override
    public String getProperty(String name) {
        return builder.build()
                .get()
                .uri(String.format("http://config-management-service/api/config-management/properties/%s?application-name=%s&application-profile=%s", name, applicationName, applicationProfile))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Void setProperty(SetPropertyRequest dto) {
        return builder.build()
                .post()
                .uri(String.format("http://config-management-service/api/config-management/properties?application-name=%s&application-profile=%s", applicationName, applicationProfile))
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ApiClientException("Client error: " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new ApiServerException("Server error: " + clientResponse.statusCode())))
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    if (e instanceof ApiClientException) {
                        return Mono.empty();
                    } else if (e instanceof ApiServerException) {
                        return Mono.empty();
                    }
                    return Mono.error(new ApiServerException("Unexpected error: " + e.getMessage()));
                })
                .block();
    }
}
