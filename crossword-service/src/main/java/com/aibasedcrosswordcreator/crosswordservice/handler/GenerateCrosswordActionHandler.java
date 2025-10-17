package com.aibasedcrosswordcreator.crosswordservice.handler;

import com.aibasedcrosswordcreator.crosswordservice.proxy.CrosswordServiceProxy;
import com.fasterxml.jackson.databind.JsonNode;

public class GenerateCrosswordActionHandler implements EndpointActionHandlerUsingService<CrosswordServiceProxy> {
    @Override
    public Object handle(JsonNode request, CrosswordServiceProxy service) {
        return service.generateCrossword(request);
    }
}
