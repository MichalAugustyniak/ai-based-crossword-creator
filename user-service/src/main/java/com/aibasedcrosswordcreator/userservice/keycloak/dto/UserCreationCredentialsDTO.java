package com.aibasedcrosswordcreator.userservice.keycloak.dto;

public record UserCreationCredentialsDTO(
        String type,
        String value,
        boolean temporary
) {
}
