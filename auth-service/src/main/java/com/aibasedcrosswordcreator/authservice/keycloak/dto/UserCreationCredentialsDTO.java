package com.aibasedcrosswordcreator.authservice.keycloak.dto;

public record UserCreationCredentialsDTO(
        String type,
        String value,
        boolean temporary
) {
}
