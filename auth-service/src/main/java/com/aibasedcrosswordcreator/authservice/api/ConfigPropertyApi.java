package com.aibasedcrosswordcreator.authservice.api;

import com.aibasedcrosswordcreator.authservice.dto.SetPropertyRequest;

public interface ConfigPropertyApi {
    public String getProperty(String name);
    public Void setProperty(SetPropertyRequest dto);
}
