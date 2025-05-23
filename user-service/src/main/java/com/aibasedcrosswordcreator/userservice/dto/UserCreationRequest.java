package com.aibasedcrosswordcreator.userservice.dto;

import jakarta.validation.constraints.Pattern;

public record UserCreationRequest(
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._]{2,19}$",
                message = "The username must be between 3 and 20 characters long, start with a letter and can only contain letters, numbers, underscores and periods.")
        String username,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
                message = "The password must be at least 8 characters long and contain a lowercase and uppercase letter, a number and a special character (e.g. @, #, $, !).")
        String password) {
}
