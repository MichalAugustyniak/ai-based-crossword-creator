package com.aibasedcrosswordcreator.crosswordservice.dto;

import java.util.List;

public record GetCrosswordsResponseDTO(int totalPages, int numberOfElements, long totalElements, List<CrosswordResponse> crosswords) {
}
