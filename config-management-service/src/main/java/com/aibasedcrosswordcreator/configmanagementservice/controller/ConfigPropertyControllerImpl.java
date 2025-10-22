package com.aibasedcrosswordcreator.configmanagementservice.controller;

import com.aibasedcrosswordcreator.configmanagementservice.dto.ConfigPropertyResponse;
import com.aibasedcrosswordcreator.configmanagementservice.dto.PropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.dto.SetPropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.service.ConfigPropertyService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config-management/properties")
@RequiredArgsConstructor
public class ConfigPropertyControllerImpl implements ConfigPropertyController {
    private final ConfigPropertyService configPropertyService;

    @GetMapping("/{name}")
    public ResponseEntity<ConfigPropertyResponse> getProperty(
            @NotBlank(message = "The property name must be not blank.")
            @PathVariable("name") String name,
            @NotBlank(message = "The application-name parameter must be not blank.")
            @RequestParam(value = "application-name") String applicationName,
            @NotBlank(message = "The application-profile parameter must be not blank.")
            @RequestParam(value = "application-profile") String applicationProfile
    ) {
        return ResponseEntity.ok(configPropertyService.getProperty(new PropertyRequest(applicationName, applicationProfile, name)));
    }

    @PostMapping
    public ResponseEntity<Void> setProperty(
            @NotNull(message = "The request body is required.")
            @RequestBody SetPropertyRequest request,
            @NotBlank(message = "The application-name parameter must be not blank.")
            @RequestParam(value = "application-name") String applicationName,
            @NotBlank(message = "The application-profile parameter must be not blank.")
            @RequestParam(value = "application-profile") String applicationProfile
    ) {
        configPropertyService.setProperty(new SetPropertyRequest(
                applicationName,
                applicationProfile,
                request.propertyName(),
                request.propertyValue()
        ));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
