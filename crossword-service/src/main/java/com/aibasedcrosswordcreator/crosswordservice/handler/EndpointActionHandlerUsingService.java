package com.aibasedcrosswordcreator.crosswordservice.handler;

import com.fasterxml.jackson.databind.JsonNode;

public interface EndpointActionHandlerUsingService<S> {
    Object handle(JsonNode request, S service);
}
