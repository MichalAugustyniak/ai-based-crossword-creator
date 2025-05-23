package com.aibasedcrosswordcreator.authservice.keycloak.dto;

public record KeycloakUserResponse(
        String id,
        long createdTimestamp,
        String username,
        boolean enabled,
        boolean emailVerified,
        String firstName,
        String lastName
) {
}
