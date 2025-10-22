package com.aibasedcrosswordcreator.configmanagementservice.controller;

import com.aibasedcrosswordcreator.configmanagementservice.dto.ConfigPropertyResponse;
import com.aibasedcrosswordcreator.configmanagementservice.dto.SetPropertyRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface ConfigPropertyController {
    ResponseEntity<ConfigPropertyResponse> getProperty(@NotBlank(message = "The property name must be not blank.") String name,
                                                       @NotBlank(message = "The application-name parameter must be not blank.") String applicationName,
                                                       @NotBlank(message = "The application-profile parameter must be not blank.") String applicationProfile);
    ResponseEntity<Void> setProperty(@NotNull(message = "The request body is required.") SetPropertyRequest dto,
                                     @NotBlank(message = "The application-name parameter must be not blank.") String applicationName,
                                     @NotBlank(message = "The application-profile parameter must be not blank.") String applicationProfile);
}
