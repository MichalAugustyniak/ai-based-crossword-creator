package com.aibasedcrosswordcreator.authservice.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    private String sub;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
}
