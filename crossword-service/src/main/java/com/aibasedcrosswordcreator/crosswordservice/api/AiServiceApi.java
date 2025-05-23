package com.aibasedcrosswordcreator.crosswordservice.api;

import com.aibasedcrosswordcreator.crosswordservice.dto.AiRequest;
import com.aibasedcrosswordcreator.crosswordservice.dto.AiResponse;

public interface AiServiceApi {
    AiResponse sendMessage(AiRequest request, String token);
}
