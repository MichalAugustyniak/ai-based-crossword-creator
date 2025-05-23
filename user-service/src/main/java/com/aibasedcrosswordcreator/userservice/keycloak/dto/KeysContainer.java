package com.aibasedcrosswordcreator.userservice.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KeysContainer {
    private List<Key> keys;
}
