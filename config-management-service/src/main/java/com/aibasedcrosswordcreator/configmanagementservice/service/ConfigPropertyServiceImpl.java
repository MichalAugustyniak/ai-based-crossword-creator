package com.aibasedcrosswordcreator.configmanagementservice.service;

import com.aibasedcrosswordcreator.configmanagementservice.dto.*;
import com.aibasedcrosswordcreator.configmanagementservice.exception.PropertyNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ConfigPropertyServiceImpl implements ConfigPropertyService {
    private static final Logger log = LoggerFactory.getLogger(ConfigPropertyServiceImpl.class);
    private final StringRedisTemplate redisTemplate;

    public ConfigPropertyResponse getProperty(@NotNull PropertyRequest request) {
        log.info("Searching for property '{}' from application '{}' of profile '{}'.", request.propertyName(), request.applicationName(), request.applicationProfile());
        String propertyValue = (String) redisTemplate.opsForHash().get(request.applicationName() + "-" + request.applicationProfile(), request.propertyName());
        if (propertyValue == null) {
            log.info("Property not found.");
            throw new PropertyNotFoundException(String.format("Property %s not found.", request.propertyName()));
        }
        log.info("Property found.");
        return new ConfigPropertyResponse(request.propertyName(), propertyValue, request.applicationName(), request.applicationProfile());
    }


    public void deleteProperty(@NotNull DeletePropertyRequest request) {
        log.info("Deleting property '{}' from application '{}' of profile '{}' if exists.", request.propertyName(), request.applicationName(), request.applicationProfile());
        redisTemplate.opsForHash()
                .delete(request.applicationName() + "-" + request.applicationProfile(), request.propertyName());
    }

    public void setProperty(@NotNull SetPropertyRequest request) {
        log.info("Putting into '{}-{}' value '{}' for name '{}'.", request.applicationName(), request.applicationProfile(), request.propertyValue(), request.propertyName());
        redisTemplate.opsForHash().put(request.applicationName() + "-" + request.applicationProfile(), request.propertyName(), request.propertyValue());
    }

    public ConfigPropertiesResponse getAll() {
        throw new NotImplementedException("This method is supposed to return all properties but it's not implemented yet");
    }
}
