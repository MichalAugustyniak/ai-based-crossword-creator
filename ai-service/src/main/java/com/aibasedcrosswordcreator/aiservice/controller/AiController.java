package com.aibasedcrosswordcreator.aiservice.controller;

import com.aibasedcrosswordcreator.aiservice.dto.AiDTO;
import com.aibasedcrosswordcreator.aiservice.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping
    public ResponseEntity<AiDTO> ai(@RequestBody AiDTO aiDTO) {
        return ResponseEntity.ok(this.aiService.ai(aiDTO));
    }
}
