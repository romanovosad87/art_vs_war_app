package com.example.artvswar.config;

import com.example.artvswar.util.AwsCognitoRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final List<String> allowedOrigins = new ArrayList<>(List.of("https://artvswar.gallery",
            "https://develop.artvswar.gallery"));

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorize ->
                        authorize.antMatchers(HttpMethod.POST, "/paintings").authenticated()
                                .antMatchers(HttpMethod.PUT, "/paintings/**").authenticated()
                                .antMatchers(HttpMethod.POST, "/authors").authenticated()
                                .antMatchers(HttpMethod.PUT, "/authors").authenticated()
                                .antMatchers(HttpMethod.GET, "/stripe/onboarding").authenticated()
                                .antMatchers(HttpMethod.POST, "/account").authenticated()
                                .antMatchers(HttpMethod.POST, "/authors/checkInputAndGet").authenticated()
                                .antMatchers(HttpMethod.GET, "/cart").authenticated()
                                .antMatchers(HttpMethod.GET, "/orders").authenticated())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());

        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AwsCognitoRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
