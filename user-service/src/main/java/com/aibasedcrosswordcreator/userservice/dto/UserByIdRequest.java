package com.aibasedcrosswordcreator.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserByIdRequest(@NotBlank(message = "The id must not be empty") String id) {
}
