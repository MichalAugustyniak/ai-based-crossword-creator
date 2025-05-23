package com.aibasedcrosswordcreator.configmanagementservice.keycloak.dto;

public record UserCreationCredentialsDTO(
        String type,
        String value,
        boolean temporary
) {
}
