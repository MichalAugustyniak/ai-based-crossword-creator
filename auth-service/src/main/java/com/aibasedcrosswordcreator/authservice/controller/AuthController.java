package com.aibasedcrosswordcreator.authservice.controller;

import com.aibasedcrosswordcreator.authservice.dto.TokenRequestDTO;
import com.aibasedcrosswordcreator.authservice.keycloak.dto.Token;
import com.aibasedcrosswordcreator.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<Token> token(@RequestBody TokenRequestDTO dto) {
        return ResponseEntity.ok(authService.getToken(dto));
    }
}
