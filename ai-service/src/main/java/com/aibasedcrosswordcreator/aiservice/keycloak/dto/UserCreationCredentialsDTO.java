package com.aibasedcrosswordcreator.aiservice.keycloak.dto;

public record UserCreationCredentialsDTO(
        String type,
        String value,
        boolean temporary
) {
}
