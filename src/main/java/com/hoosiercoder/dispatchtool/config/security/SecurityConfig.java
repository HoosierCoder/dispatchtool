package com.hoosiercoder.dispatchtool.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Author: HoosierCoder
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow static resources for everyone
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // 2. Platform-level routes (rdude only)
                        .requestMatchers("/system/**").hasRole("SYSTEM_ADMIN")

                        // 3. REST API routes - Allow SYSTEM_ADMIN and Tenant ADMINs
                        .requestMatchers("/api/**").hasAnyRole("SYSTEM_ADMIN", "ADMIN")

                        // 4. Tenant-level routes
                        // We allow SYSTEM_ADMIN here so you can impersonate or troubleshoot
                        .requestMatchers("/tenant/**").hasAnyRole("SYSTEM_ADMIN", "ADMIN")

                        // 5. Everything else requires a login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Enable HTTP Basic Authentication for API clients (like Postman)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
