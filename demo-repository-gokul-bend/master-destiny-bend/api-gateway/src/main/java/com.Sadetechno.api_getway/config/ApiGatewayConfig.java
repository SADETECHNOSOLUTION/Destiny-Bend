package com.Sadetechno.api_getway.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Configuration
public class ApiGatewayConfig {

    @Value("${jwt.secret}")
    private String secretString;

    private JwtDecoder jwtDecoder;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(new SecretKeySpec(keyBytes, "HmacSHA256")).build();
    }

    @Bean
    public KeyResolver jwtKeyResolver() {
        return exchange -> {
            // Extract the JWT token from the Authorization header
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
                return Mono.just(validateTokenAndGetUsername(token));
            }
            return Mono.just("anonymous"); // Default value if no JWT token is provided
        };
    }

    // Helper function to extract and validate the username from the JWT token
    private String validateTokenAndGetUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        if (isTokenExpired(jwt)) {
            throw new RuntimeException("Token has expired");
        }
        return jwt.getClaimAsString("sub"); // "sub" is the claim for the username (or use any other claim you need)
    }

    // Helper function to check if the token is expired
    private boolean isTokenExpired(Jwt jwt) {
        Date expiration = Date.from(Objects.requireNonNull(jwt.getExpiresAt()));
        return expiration.before(new Date());
    }


}
