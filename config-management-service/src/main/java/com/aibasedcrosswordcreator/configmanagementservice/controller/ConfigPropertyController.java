package com.aibasedcrosswordcreator.configmanagementservice.controller;

import com.aibasedcrosswordcreator.configmanagementservice.dto.SetPropertyRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface ConfigPropertyController {
    ResponseEntity<String> getProperty(@NotBlank String name, @NotBlank String applicationName, @NotBlank String applicationProfile);
    ResponseEntity<Void> setProperty(@NotNull SetPropertyRequest dto, @NotBlank String applicationName, @NotBlank String applicationProfile);
}
