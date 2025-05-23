package com.aibasedcrosswordcreator.configmanagementservice.service;

import com.aibasedcrosswordcreator.configmanagementservice.dto.ConfigPropertyResponse;
import com.aibasedcrosswordcreator.configmanagementservice.dto.DeletePropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.dto.PropertyRequest;
import com.aibasedcrosswordcreator.configmanagementservice.dto.SetPropertyRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ConfigPropertyService {
    String getProperty(@NotNull PropertyRequest dto);

    void deleteProperty(@NotNull DeletePropertyRequest dto);

    void setProperty(@NotNull SetPropertyRequest dto);

    List<ConfigPropertyResponse> getAll();
}
