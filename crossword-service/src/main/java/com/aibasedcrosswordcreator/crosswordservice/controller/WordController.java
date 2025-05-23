package com.aibasedcrosswordcreator.crosswordservice.controller;

import com.aibasedcrosswordcreator.crosswordservice.dto.GenerateWordsRequestDTO;
import com.aibasedcrosswordcreator.crosswordservice.dto.SaveWordsAndGenerateCluesRequest;
import com.aibasedcrosswordcreator.crosswordservice.dto.SaveWordsWithCluesRequest;
import com.aibasedcrosswordcreator.crosswordservice.dto.WordsResponse;
import com.aibasedcrosswordcreator.crosswordservice.service.WordService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
@Validated
public class WordController {
    private final WordService wordService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/generate")
    public ResponseEntity<WordsResponse> generateWords(@NotNull @RequestBody GenerateWordsRequestDTO generateWordsRequestDTO) {
        WordsResponse response = wordService.generateWords(generateWordsRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save-and-generate-clues")
    public ResponseEntity<Void> saveWithClues(@NotNull @RequestBody SaveWordsAndGenerateCluesRequest saveWordsAndGenerateCluesDTO) {
        wordService.saveWordsAndGenerateClues(saveWordsAndGenerateCluesDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-with-clues")
    public ResponseEntity<Void> saveAndGenerateClues(@NotNull @RequestBody SaveWordsWithCluesRequest dto) {
        wordService.addWordsWithClues(dto);
        return ResponseEntity.ok().build();
    }
}
