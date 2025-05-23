package com.aibasedcrosswordcreator.crosswordservice.controller;

import com.aibasedcrosswordcreator.crosswordservice.dto.*;
import com.aibasedcrosswordcreator.crosswordservice.service.CrosswordService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/crosswords")
@RequiredArgsConstructor
@Validated
public class CrosswordController {
    private final CrosswordService crosswordService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/create-with-ai")
    public ResponseEntity<CrosswordResponse> createCrosswordWithAi(@NotNull @RequestBody GenerateCrosswordRequest request) throws Exception {
        CrosswordResponse response = crosswordService.generateWithAi(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-without-ai")
    public ResponseEntity<CrosswordResponse> createCrosswordWithoutAi(@NotNull @RequestBody GenerateCrosswordRequest request) {
        CrosswordResponse response = crosswordService.generateWithoutAi(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CrosswordResponse> getCrossword(@NotNull @PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(crosswordService.getCrosswordByUuid(new CrosswordRequest(uuid)));
    }

    @GetMapping
    public ResponseEntity<GetCrosswordsResponseDTO> getCrosswords(
            @RequestParam(required = false) String creator,
            @RequestParam(required = false) Integer height,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(100) int size
    ) {
        return ResponseEntity.ok(crosswordService.getCrosswords(new GetCrosswordsRequestDTO(
                size,
                page,
                height,
                width,
                provider,
                model,
                creator,
                language
        )));
    }
}
