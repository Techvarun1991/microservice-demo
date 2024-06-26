package com.example.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;

    public CustomOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        this.delegate = new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);

        Map<String, Object> attributes = principal.getAttributes();
        Collection<GrantedAuthority> authorities = extractAuthorities(attributes);

        return new OAuth2IntrospectionAuthenticatedPrincipal(attributes, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Map<String, Object> attributes) {
        Map<String, Object> resourceAccess = (Map<String, Object>) attributes.get("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("ecommerce-microservices");
        if (clientAccess == null) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) clientAccess.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
