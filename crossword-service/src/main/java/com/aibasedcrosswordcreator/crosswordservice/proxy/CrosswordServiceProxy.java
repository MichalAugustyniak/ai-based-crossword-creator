package com.aibasedcrosswordcreator.crosswordservice.proxy;

import com.fasterxml.jackson.databind.JsonNode;

public interface CrosswordServiceProxy {
    Object getCrosswords(JsonNode request);

    Object getCrossword(JsonNode request);

    Object generateCrossword(JsonNode request);

    Object saveCrossword(JsonNode request);
}
