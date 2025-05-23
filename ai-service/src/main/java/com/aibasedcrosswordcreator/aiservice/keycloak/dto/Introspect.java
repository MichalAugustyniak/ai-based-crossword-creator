package com.aibasedcrosswordcreator.aiservice.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Introspect {
    private int exp;
    private int iat;
    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    @JsonProperty("session_state")
    private String sessionState;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    private String acr;
    @JsonProperty("allowed-origins")
    private List<String> allowedOrigins;
    @JsonProperty("realm_access")
    private RealmAccess realmAccess;
    @JsonProperty("resource_access")
    private ResourceAccess resourceAccess;
    private String scope;
    private String sid;
    @JsonProperty("client_id")
    private String clientId;
    private String username;
    private boolean active;
}
