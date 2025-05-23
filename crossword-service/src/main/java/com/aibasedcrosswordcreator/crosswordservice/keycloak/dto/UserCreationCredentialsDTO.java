package com.aibasedcrosswordcreator.crosswordservice.keycloak.dto;

public record UserCreationCredentialsDTO(
        String type,
        String value,
        boolean temporary
) {
}
