package com.aibasedcrosswordcreator.aiservice.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Key {
    private String kid;
    private String kty;
    private String alg;
    private String use;
    private String n;
    private String e;
    private List<String> x5c;
    private String x5t;
    @JsonProperty("x5t#S256")
    private String x5tS256;
}
