package com.aibasedcrosswordcreator.authservice.keycloak.api;

import com.aibasedcrosswordcreator.authservice.keycloak.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class KeycloakApi {
    private final String clientId;
    private final String clientSecret;
    private final String hostUrl;
    private final String adminUsername;
    private final String adminPassword;
    private final String realmName;

    public KeycloakApi(String clientId, String clientSecret, String hostUrl, String adminUsername, String adminPassword, String realmName) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.hostUrl = hostUrl;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.realmName = realmName;
    }

    public Token getToken(String username, String password, String grantType) {
        WebClient webClient = WebClient.create(hostUrl);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("password", password);
        map.add("username", username);
        map.add("scope", "openid");
        return webClient.post()
                .uri("/realms/app/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(map)
                .retrieve()
                .bodyToMono(Token.class)
                .block();
    }

    public KeysContainer getKeys() {
        WebClient webClient = WebClient.create(hostUrl);
        return webClient.get()
                .uri("/realms/app/protocol/openid-connect/certs")
                .retrieve()
                .bodyToMono(KeysContainer.class)
                .block();
    }

    public Introspect introspect(String token, String grantType) {
        WebClient webClient = WebClient.create(hostUrl);
        String body = String.format("grant_type=%s&client_id=%s&client_secret=%s&token=%s",
                grantType, clientId, clientSecret, token);
        return webClient.post()
                .uri("/realms/app/protocol/openid-connect/token/introspect")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Introspect.class)
                .block();
    }

    public UserInfo userInfo(String token) {
        WebClient webClient = WebClient.create(hostUrl);
        return webClient.get()
                .uri("/realms/app/protocol/openid-connect/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
    }

    public void createUser(SaveUserDTO dto) throws JsonProcessingException {
        Token token = getToken(adminUsername, adminPassword, "client_credentials");
        ObjectMapper objectMapper = new ObjectMapper();
        WebClient webClient = WebClient.create(hostUrl);
        webClient.post()
                .uri(String.format("/admin/realms/%s/users", realmName))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .bodyValue(objectMapper.writeValueAsString(dto))
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(), response -> {
                    throw new RuntimeException("Something went wrong while creating a new user");
                });
    }

    public List<KeycloakUserResponse> getUsers(String fieldName, String fieldValue) {
        Token token = getToken(adminUsername, adminPassword, "client_credentials");
        WebClient webClient = WebClient.create(hostUrl);
        return webClient.get()
                .uri(String.format("/admin/realms/%s/users?%s=%s", realmName, fieldName, fieldValue))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<KeycloakUserResponse>>() {
                })
                .block();
    }

    public List<KeycloakUserRoleMapping> getUserRoleMappings(String id) {
        Token token = getToken(adminUsername, adminPassword, "client_credentials");
        WebClient webClient = WebClient.create(hostUrl);
        return webClient.get()
                .uri(String.format("/admin/realms/app/users/%s/role-mappings/realm", id))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<KeycloakUserRoleMapping>>() {
                })
                .block();
    }

    public KeycloakUserResponse getUser(String id) {
        Token token = getToken(adminUsername, adminPassword, "client_credentials");
        WebClient webClient = WebClient.create(hostUrl);
        return webClient.get()
                .uri(String.format("/admin/realms/%s/users/%s", realmName, id))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .retrieve()
                .bodyToMono(KeycloakUserResponse.class)
                .block();
    }
}
