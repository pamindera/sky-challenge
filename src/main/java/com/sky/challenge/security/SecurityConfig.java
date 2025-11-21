package com.sky.challenge.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/*").authenticated()
                        .requestMatchers("/api/v1/user/*/project/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
//@Bean
//public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
//    return http
//            .securityMatcher("/**") // applies to all other requests
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
//                    .anyRequest().permitAll()
//            )
//            .csrf(AbstractHttpConfigurer::disable)
//            .build();
//}
//
//    @Bean
//    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .securityMatcher("/api/v1/user/**") // <--- only applies to /api/v1/user endpoints
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/user/*/project/**").authenticated()
//                        .requestMatchers("/api/v1/user/*").authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
