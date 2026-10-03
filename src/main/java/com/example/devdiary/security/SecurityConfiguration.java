package com.example.devdiary.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Value("${v1API}")
    private String apiVersion;

    @Value("${allowedOrigins}")
    private String allowedOrigins;

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(
                List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(
                Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .filter(origin -> !origin.isEmpty())
                        .toList());
        corsConfiguration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http.cors()
                .configurationSource(request -> corsConfiguration)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(apiVersion + "/signin", apiVersion + "/signup")
                .permitAll()
                .antMatchers(HttpMethod.GET, apiVersion + "/stories/my", apiVersion + "/users/me")
                .authenticated()
                .antMatchers(
                        HttpMethod.GET,
                        apiVersion + "/stories/",
                        apiVersion + "/stories/*",
                        apiVersion + "/stories/*/comments",
                        apiVersion + "/stories/*/likes")
                .permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        apiVersion + "/users/",
                        apiVersion + "/users/*",
                        apiVersion + "/users/*/stories")
                .permitAll()
                .antMatchers(HttpMethod.GET, apiVersion + "/tags/", apiVersion + "/tags/*/stories")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
