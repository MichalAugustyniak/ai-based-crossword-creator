package com.aibasedcrosswordcreator.aiservice.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Account {
    private List<String> roles;
}
