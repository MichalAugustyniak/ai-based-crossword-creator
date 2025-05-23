package com.aibasedcrosswordcreator.configmanagementservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ConfigPropertiesResponse(@NotNull List<@NotNull ConfigPropertyResponse> configProperties) {
}
