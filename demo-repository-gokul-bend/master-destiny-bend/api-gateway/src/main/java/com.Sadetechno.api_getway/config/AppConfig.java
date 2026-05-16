package com.Sadetechno.api_getway.config;

import com.Sadetechno.api_getway.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthFilter jwtAuthFilter) {
        return builder.routes()
                .route("jwt-module", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://jwt-module"))
                .route("user-module", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://user-module"))
                .route("post-module", r -> r.path("/posts/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://post-module"))
                .route("like-module", r -> r.path("/likes/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://like-module"))
                .route("comment-module", r -> r.path("/comments/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://comment-module"))
                .route("status-module", r -> r.path("/statuses/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://status-module"))
                .route("follow-module", r -> r.path("/follows/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://follow-module"))
                .route("reels-module", r -> r.path("/reels/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://reels-module"))
                .route("friend-request-module", r -> r.path("/friend-requests/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://friend-request-module"))
                .route("websocket-messaging", r -> r.path("/ws/**")
                        .or().path("/web-socket/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://websocket-messaging"))
                .route("notification-module", r -> r.path("/notification/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://notification-module"))
                .route("home_api_module", r -> r.path("/home/api/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://localhost:8093"))
                .route("permission-module", r -> r.path("/permissions/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://localhost:8094"))
                .route("settings",r -> r.path("/settings/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://settings"))
                .route("media-aggregate-service", r -> r.path("/aggregate-media/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://media-aggregate-service"))
                .route("Actuator", r -> r.path("/actuator/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://Actuator"))
                .route("profession",r -> r.path("/api/profession/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://profession"))
                .build();
    }
}
