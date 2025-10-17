package com.aibasedcrosswordcreator.crosswordservice.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public class UserUtil {
    public static Optional<String> getUserIdBySecurityContext() {
        String id = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            JwtAuthenticationToken token = (JwtAuthenticationToken) securityContext.getAuthentication();
            if (token.isAuthenticated()) {
                id = (String) token.getTokenAttributes().get(JwtClaimNames.SUB);
            }
        } catch (ClassCastException ignored) {

        }
        return Optional.ofNullable(id);
    }
}
