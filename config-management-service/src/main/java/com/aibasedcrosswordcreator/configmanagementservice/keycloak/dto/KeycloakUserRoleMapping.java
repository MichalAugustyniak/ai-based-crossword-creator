package com.aibasedcrosswordcreator.configmanagementservice.keycloak.dto;

public record KeycloakUserRoleMapping(
        String id,
        String name,
        String description,
        boolean composite,
        boolean clientRole,
        String containerId
) {
}
