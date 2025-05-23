package com.aibasedcrosswordcreator.aiservice.communication.model;

public interface Chat {
    String message(String message);

    String getName();

    String getProvider();

    Chat newInstance();
}
