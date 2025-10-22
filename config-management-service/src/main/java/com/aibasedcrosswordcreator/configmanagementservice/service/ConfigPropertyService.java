package com.aibasedcrosswordcreator.configmanagementservice.service;

import com.aibasedcrosswordcreator.configmanagementservice.dto.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ConfigPropertyService {
    ConfigPropertyResponse getProperty(@NotNull PropertyRequest dto);

    void deleteProperty(@NotNull DeletePropertyRequest dto);

    void setProperty(@NotNull SetPropertyRequest dto);

    ConfigPropertiesResponse getAll();
}
