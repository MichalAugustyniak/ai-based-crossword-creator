package com.aibasedcrosswordcreator.crosswordservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WordsResponse(@NotNull List<@NotNull WordAndClueDTO> words) {
}
