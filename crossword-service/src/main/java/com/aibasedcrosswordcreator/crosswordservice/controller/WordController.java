package com.aibasedcrosswordcreator.crosswordservice.controller;

import com.aibasedcrosswordcreator.crosswordservice.registry.EndpointActionHandlerUsingServiceRegistry;
import com.aibasedcrosswordcreator.crosswordservice.service.WordService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@Validated
public class WordController {
    private final WordService wordService;
    private final EndpointActionHandlerUsingServiceRegistry<WordService> registry;

    @PostMapping
    public ResponseEntity<Object> createWords(@NotNull @RequestBody JsonNode request, @RequestParam String action) {
        var handler = registry.getHandler(action);
        var response = handler.handle(request, wordService);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
