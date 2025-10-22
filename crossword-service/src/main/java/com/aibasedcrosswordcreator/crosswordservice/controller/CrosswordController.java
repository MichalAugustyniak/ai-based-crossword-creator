package com.aibasedcrosswordcreator.crosswordservice.controller;

import com.aibasedcrosswordcreator.crosswordservice.dto.*;
import com.aibasedcrosswordcreator.crosswordservice.registry.CrosswordServiceProxyRegistry;
import com.aibasedcrosswordcreator.crosswordservice.registry.EndpointActionHandlerUsingServiceRegistry;
import com.aibasedcrosswordcreator.crosswordservice.proxy.CrosswordServiceProxy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/crosswords")
@RequiredArgsConstructor
@Validated
public class CrosswordController {
    private final CrosswordServiceProxyRegistry serviceProxyRegistry;
    private final EndpointActionHandlerUsingServiceRegistry<CrosswordServiceProxy> actionHandlerRegistry;

    @PostMapping
    public ResponseEntity<Object> createCrossword(
            @RequestParam String action,
            @RequestParam String type,
            @NotNull @RequestBody JsonNode requestBody
    ) {
        var service = serviceProxyRegistry.getCrosswordServiceProxy(type);
        var handler = actionHandlerRegistry.getHandler(action);
        return new ResponseEntity<>(handler.handle(requestBody, service), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getCrossword(@NotNull @PathVariable("uuid") UUID uuid) {
        var service = serviceProxyRegistry.getAny();
        var objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.valueToTree(new StandardCrosswordRequest(uuid));
        return ResponseEntity.ok(service.getCrossword(jsonNode));
    }

    @GetMapping
    public ResponseEntity<CrosswordResponse> getCrosswords(
            @RequestParam(required = false) String creator,
            @RequestParam(required = false) Integer height,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            @RequestParam(required = false) String type
    ) {
        var request = new GetCrosswordsRequestDTO(
                size,
                page,
                height,
                width,
                provider,
                model,
                creator,
                language
        );
        var objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.valueToTree(request);
        var service = serviceProxyRegistry.getCrosswordServiceProxy(type);
        var crosswords = service.getCrosswords(jsonNode);
        return ResponseEntity.ok();
    }
}

