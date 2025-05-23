package com.aibasedcrosswordcreator.configmanagementservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aibasedcrosswordcreator.configmanagementservice.dto.ConfigPropertyResponse;
import com.aibasedcrosswordcreator.configmanagementservice.dto.DeletePropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.dto.PropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.dto.SetPropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.exception.JsonSerializationException;
import com.aibasedcrosswordcreator.configmanagementservice.exception.PropertyNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class ConfigPropertyServiceImpl implements ConfigPropertyService {
    private static final Logger log = LoggerFactory.getLogger(ConfigPropertyServiceImpl.class);
    private final RedisTemplate<String, String> redisTemplate;

    public String getProperty(@NotNull PropertyRequest request) {
        log.info("Searching for property '{}' from application '{}' of profile '{}'.", request.propertyName(), request.applicationName(), request.applicationProfile());
        String propertyValue = (String) redisTemplate.opsForHash().get(request.applicationName() + "-" + request.applicationProfile(), request.propertyName());
        if (propertyValue == null) {
            log.info("Property not found.");
            throw new PropertyNotFoundException(String.format("Property %s not found.", request.propertyName()));
        }
        log.info("Property found.");
        return propertyValue;
    }


    public void deleteProperty(@NotNull DeletePropertyRequest request) {
        log.info("Deleting property '{}' from application '{}' of profile '{}' if exists.", request.propertyName(), request.applicationName(), request.applicationProfile());
        redisTemplate.opsForHash()
                .delete(request.applicationName() + "-" + request.applicationProfile(), request.propertyName());
    }

    public void setProperty(@NotNull SetPropertyRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(request.propertyValue());
            log.info("Putting into '{}-{}' value '{}' for name '{}'.", request.applicationName(), request.applicationProfile(), request.propertyValue(), request.propertyName());
            redisTemplate.opsForHash().put(request.applicationName() + "-" + request.applicationProfile(), request.propertyName(), json);
        } catch (JsonProcessingException e) {
            log.info("Request serialization failed.");
            throw new JsonSerializationException("Error serializing property value for name: " + request.propertyName(), e);
        }
    }

    public List<ConfigPropertyResponse> getAll() {
        throw new NotImplementedException("This method is supposed to return all properties but it's not implemented yet");
    }
}
