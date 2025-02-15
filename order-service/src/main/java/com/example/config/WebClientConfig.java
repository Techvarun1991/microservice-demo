package com.example.config;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	 @Bean
	    @LoadBalanced
	    public WebClient.Builder webClientBuilder() {
	        return WebClient.builder().filter(authenticationFilter());
	    }

	    private ExchangeFilterFunction authenticationFilter() {
	        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
	            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	            if (authentication instanceof BearerTokenAuthentication) {
	                BearerTokenAuthentication bearerTokenAuth = (BearerTokenAuthentication) authentication;
	                String token = bearerTokenAuth.getToken().getTokenValue();
	                ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
	                        .header("Authorization", "Bearer " + token)
	                        .build();
	                return Mono.just(authorizedRequest);
	            }
	            return Mono.just(clientRequest);
	        });
	    }
	
	
//	@Bean
//	@LoadBalanced
//	public WebClient.Builder webClientBuilder() {
//		return WebClient.builder();
//	}
}
