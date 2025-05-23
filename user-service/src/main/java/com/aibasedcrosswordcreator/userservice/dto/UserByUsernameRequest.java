package com.aibasedcrosswordcreator.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserByUsernameRequest(@NotBlank(message = "The username must not be empty") String username) {
}
