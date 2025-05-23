package com.aibasedcrosswordcreator.authservice.service;

import com.aibasedcrosswordcreator.authservice.dto.TokenRequestDTO;
import com.aibasedcrosswordcreator.authservice.keycloak.api.KeycloakApi;
import com.aibasedcrosswordcreator.authservice.keycloak.dto.Token;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {
    private final KeycloakApi keycloakApi;

    public Token getToken(@NotNull TokenRequestDTO request) {
        return keycloakApi.getToken(request.username(), request.password(), "password");
    }
}
