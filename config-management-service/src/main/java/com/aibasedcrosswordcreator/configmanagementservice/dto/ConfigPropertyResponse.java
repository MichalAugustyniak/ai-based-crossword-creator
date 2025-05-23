package com.aibasedcrosswordcreator.configmanagementservice.dto;

public record ConfigPropertyResponse(
        String propertyName,
        String propertyValue,
        String applicationName,
        String applicationProfile
) {
}
