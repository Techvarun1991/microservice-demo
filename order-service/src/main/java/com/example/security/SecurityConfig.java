package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(opaqueToken -> {
                opaqueToken.introspector(customOpaqueTokenIntrospector());
            }))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public OpaqueTokenIntrospector customOpaqueTokenIntrospector() {
        return new CustomOpaqueTokenIntrospector(
                "http://localhost:8080/realms/demo/protocol/openid-connect/token/introspect",
                "ecommerce-microservices",
                "2UdAoD5Dswb4ykWUhHNiMkqJ3DCsrY58"
        		);
    }
}




