package com.aibasedcrosswordcreator.userservice.controller;

import com.aibasedcrosswordcreator.userservice.dto.UserByIdRequest;
import com.aibasedcrosswordcreator.userservice.dto.UserByUsernameRequest;
import com.aibasedcrosswordcreator.userservice.dto.UserCreationRequest;
import com.aibasedcrosswordcreator.userservice.dto.UserResponse;
import com.aibasedcrosswordcreator.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@NotNull Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String id = (String) token.getTokenAttributes().get(JwtClaimNames.SUB);
        log.info("Fetching user data for id: {}", id);
        return ResponseEntity.ok(userService.getUser(new UserByIdRequest(id)));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@NotNull @PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(new UserByUsernameRequest(username)));
    }

    @PostMapping
    public ResponseEntity<Void> saveUser(@NotNull @RequestBody UserCreationRequest request) throws JsonProcessingException {
        userService.createUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
