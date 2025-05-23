package com.aibasedcrosswordcreator.crosswordservice.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RealmAccess {
    private List<String> roles;
}
