package com.example.devdiary.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsInfo userDetailsInfo;

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${v1API}")
    private String apiVersion;

    @Value("${allowedOrigins}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        List<String> origins=new ArrayList<>();
        for(String origin : allowedOrigins.split(",")){
            if(!origin.trim().isEmpty()) origins.add(origin.trim());
        }
        corsConfiguration.setAllowedOrigins(origins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http.cors().configurationSource(request -> corsConfiguration).and().csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(apiVersion+"/signin",apiVersion+"/signup")
                .permitAll()
                .antMatchers(HttpMethod.GET,apiVersion+"/stories/my",apiVersion+"/users/me")
                .authenticated()
                .antMatchers(HttpMethod.GET,apiVersion+"/stories/",apiVersion+"/stories/*",
                        apiVersion+"/stories/*/comments",apiVersion+"/stories/*/likes")
                .permitAll()
                .antMatchers(HttpMethod.GET,apiVersion+"/users/",apiVersion+"/users/*",apiVersion+"/users/*/stories")
                .permitAll()
                .antMatchers(HttpMethod.GET,apiVersion+"/tags/",apiVersion+"/tags/*/stories")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
