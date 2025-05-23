package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CrosswordRequest(@NotNull UUID uuid) {
}
