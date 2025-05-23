package com.aibasedcrosswordcreator.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(@NotBlank(message = "The username must not be empty") String username) {
}
