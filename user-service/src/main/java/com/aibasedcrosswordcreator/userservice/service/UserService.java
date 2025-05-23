package com.aibasedcrosswordcreator.userservice.service;

import com.aibasedcrosswordcreator.userservice.dto.*;
import com.aibasedcrosswordcreator.userservice.exception.NotImplementedException;
import com.aibasedcrosswordcreator.userservice.exception.UserNotFoundException;
import com.aibasedcrosswordcreator.userservice.keycloak.api.KeycloakApi;
import com.aibasedcrosswordcreator.userservice.keycloak.dto.KeycloakUserResponse;
import com.aibasedcrosswordcreator.userservice.keycloak.dto.KeycloakUserRoleMapping;
import com.aibasedcrosswordcreator.userservice.keycloak.dto.SaveUserDTO;
import com.aibasedcrosswordcreator.userservice.keycloak.dto.UserCreationCredentialsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
    private final KeycloakApi keycloakApi;

    public UserService(KeycloakApi keycloakApi) {
        this.keycloakApi = keycloakApi;
    }

    public UserResponse getUser(@NotNull UserByUsernameRequest request) {
        List<KeycloakUserResponse> users = keycloakApi.getUsers("username", request.username());
        if (users.isEmpty()) {
            throw new UserNotFoundException(String.format("User %s not found", request.username()));
        }
        KeycloakUserResponse user = users.get(0);
        List<KeycloakUserRoleMapping> roleMappings = keycloakApi.getUserRoleMappings(user.id());
        return new UserResponse(
                user.id(),
                user.username(),
                roleMappings.stream()
                        .map(KeycloakUserRoleMapping::name)
                        .collect(Collectors.toSet())
        );
    }

    public UserResponse getUser(@NotNull UserByIdRequest request) {
        var user = keycloakApi.getUser(request.id());
        var roleMappings = keycloakApi.getUserRoleMappings(user.id());
        return new UserResponse(
                user.id(),
                user.username(),
                roleMappings.stream()
                        .map(KeycloakUserRoleMapping::name)
                        .collect(Collectors.toSet())
        );
    }

    public void updateUser(@NotNull UserUpdateRequest request) {
        throw new NotImplementedException();
    }

    public void createUser(@NotNull UserCreationRequest request) throws JsonProcessingException {
        List<UserCreationCredentialsDTO> credentials = new ArrayList<>();
        credentials.add(new UserCreationCredentialsDTO("password", request.password(), false));
        SaveUserDTO payload = new SaveUserDTO(
                request.username(),
                true,
                null,
                null,
                null,
                credentials
        );
        keycloakApi.createUser(payload);
    }
}
