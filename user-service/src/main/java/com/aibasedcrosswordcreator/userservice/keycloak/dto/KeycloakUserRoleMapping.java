package com.aibasedcrosswordcreator.userservice.keycloak.dto;

public record KeycloakUserRoleMapping(
        String id,
        String name,
        String description,
        boolean composite,
        boolean clientRole,
        String containerId
) {
}
