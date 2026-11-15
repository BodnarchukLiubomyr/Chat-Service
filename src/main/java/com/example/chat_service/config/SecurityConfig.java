package com.example.chat_service.config;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private RSAKey rsaKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/create-chat/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/get-chats/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/get-chatsByName/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/chat/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/chatUnread/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/unread-message/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/send/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/update-message/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/delete-message/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/group/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/group-send/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/group-history/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/update-group-message/**").permitAll()
                        .requestMatchers("/api/v1/chat-service/delete-group-message/**").permitAll()
                        .anyRequest().hasAuthority("SCOPE_USER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
