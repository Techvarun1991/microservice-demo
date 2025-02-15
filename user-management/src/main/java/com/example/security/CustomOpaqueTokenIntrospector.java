package com.example.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	 private final OpaqueTokenIntrospector delegate;

    public CustomOpaqueTokenIntrospector() {
        this.delegate = new NimbusOpaqueTokenIntrospector(
                "http://localhost:8080/realms/demo/protocol/openid-connect/token/introspect",
                "ecommerce-microservices",
                "2ciztmwmwCZwJfUPuVFx5vpjrd5oT0eS");
    }
    
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);

        Map<String, Object> attributes = principal.getAttributes();
        Collection<GrantedAuthority> authorities = extractAuthorities(attributes);

        return new OAuth2IntrospectionAuthenticatedPrincipal(attributes, authorities);
    }
    
    private Collection<GrantedAuthority> extractAuthorities(Map<String, Object> attributes) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Extract roles from realm_access
        Map<String, Object> realmAccess = (Map<String, Object>) attributes.get("realm_access");
        if (realmAccess != null) {
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            if (roles != null) {
                roles.forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
            }
        }

        return authorities;
    }

    
    public void validateUserId(String token, String expectedUserId) {
        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
        String actualUserId = (String) principal.getAttribute("sub");

        if (!actualUserId.equals(expectedUserId)) {
            throw new RuntimeException("User ID does not match the token's subject.");
        }
    }

}
