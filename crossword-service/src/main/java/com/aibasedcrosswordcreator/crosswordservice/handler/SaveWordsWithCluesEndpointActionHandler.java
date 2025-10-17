package com.aibasedcrosswordcreator.crosswordservice.handler;

import com.aibasedcrosswordcreator.crosswordservice.dto.SaveWordsWithCluesRequest;
import com.aibasedcrosswordcreator.crosswordservice.exception.ServerException;
import com.aibasedcrosswordcreator.crosswordservice.service.WordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveWordsWithCluesEndpointActionHandler implements EndpointActionHandlerUsingService<WordService> {
    @Override
    public Object handle(JsonNode request, WordService service) {
        var objectMapper = new ObjectMapper();
        try {
            var mappedRequest = objectMapper.treeToValue(request, SaveWordsWithCluesRequest.class);
            service.addWordsWithClues(mappedRequest);
        } catch (JsonProcessingException e) {
            throw new ServerException("Cannot convert provided json node.");
        }
        return null;
    }
}
