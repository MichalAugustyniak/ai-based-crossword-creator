package com.aibasedcrosswordcreator.userservice.config;

import com.aibasedcrosswordcreator.userservice.keycloak.api.KeycloakApi;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Bean
    public Keycloak keycloak(
            @Value("${keycloak.server.url}") String serverUrl,
            @Value("${keycloak.realm.name}") String realmName,
            @Value("${keycloak.realm.client.id}") String clientId,
            @Value("${keycloak.realm.client.secret}") String clientSecret,
            @Value("${keycloak.admin.username}") String username,
            @Value("${keycloak.admin.password}") String password
    ) {
        return Keycloak.getInstance(
                serverUrl,
                realmName,
                username,
                password,
                clientId,
                clientSecret
        );
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak, @Value("keycloak.realm.name") String realm) {
        return keycloak.realm(realm);
    }

    @Bean
    public KeycloakApi keycloakApi(
            @Value("${keycloak.realm.client.id}") String clientId,
            @Value("${keycloak.server.url}") String hostUrl,
            @Value("${keycloak.realm.client.secret}") String clientSecret,
            @Value("${keycloak.admin.username}") String username,
            @Value("${keycloak.admin.password}") String password,
            @Value("${keycloak.realm.name}") String realmName
    ) {
        return new KeycloakApi(clientId, clientSecret, hostUrl, username, password, realmName);
    }
}
