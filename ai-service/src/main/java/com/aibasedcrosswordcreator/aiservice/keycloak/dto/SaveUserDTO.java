package com.aibasedcrosswordcreator.aiservice.keycloak.dto;

import java.util.List;

public record SaveUserDTO(
        String username,
        boolean enabled,
        String email,
        String firstName,
        String lastName,
        List<UserCreationCredentialsDTO> credentials
) {
}
