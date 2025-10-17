package com.aibasedcrosswordcreator.crosswordservice.proxy;

import com.aibasedcrosswordcreator.crosswordservice.dto.GenerateStandardCrosswordRequest;
import com.aibasedcrosswordcreator.crosswordservice.dto.GetCrosswordsRequestDTO;
import com.aibasedcrosswordcreator.crosswordservice.dto.StandardCrosswordRequest;
import com.aibasedcrosswordcreator.crosswordservice.exception.ServerException;
import com.aibasedcrosswordcreator.crosswordservice.service.StandardCrosswordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StandardCrosswordServiceProxy implements CrosswordServiceProxy {
    private final StandardCrosswordService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object getCrosswords(JsonNode request) {
        try {
            var mappedRequest = objectMapper.treeToValue(request, GetCrosswordsRequestDTO.class);
            return service.getCrosswords(mappedRequest);
        } catch (JsonProcessingException e) {
            throw new ServerException("Cannot convert provided json node.");
        }
    }

    @Override
    public Object getCrossword(JsonNode request) {
        try {
            var mappedRequest = objectMapper.treeToValue(request, StandardCrosswordRequest.class);
            return service.getCrosswordByUuid(mappedRequest);
        } catch (JsonProcessingException e) {
            throw new ServerException("Cannot convert provided json node.");
        }
    }

    @Override
    public Object generateCrossword(JsonNode request) {
        try {
            var mappedRequest = objectMapper.treeToValue(request, GenerateStandardCrosswordRequest.class);
            return service.generateCrossword(mappedRequest);
        } catch (JsonProcessingException e) {
            throw new ServerException("Cannot convert provided json node.");
        }
    }

    @Override
    public Object saveCrossword(JsonNode request) {
        return null;
    }
}
