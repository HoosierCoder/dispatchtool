package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.config.filter.TenantFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Import this

/**
 * Author: HoosierCoder
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Empty constructor, as all dependencies will be injected into @Bean methods
    public SecurityConfig() {
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Removed the explicit TenantAuthenticationProvider @Bean method.
    // It will be created directly within the filterChain method.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomAuthenticationSuccessHandler successHandler,
                                           TenantFilter tenantFilter,
                                           TenantAuthenticationDetailsSource tenantAuthenticationDetailsSource,
                                           CustomUserDetailsService customUserDetailsService, // Inject CustomUserDetailsService
                                           PasswordEncoder passwordEncoder) throws Exception { // Inject PasswordEncoder
        // Build AuthenticationManager here to use our custom provider
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new TenantAuthenticationProvider(customUserDetailsService, passwordEncoder));
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                // Configure AuthenticationManager
                .authenticationManager(authenticationManager)
                // Add the TenantFilter BEFORE the UsernamePasswordAuthenticationFilter
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
                // Disable CSRF for API endpoints as they are stateless/Basic Auth
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow static resources for everyone
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // 2. Platform-level routes (rdude only)
                        .requestMatchers("/system/**").hasRole("SYSTEM_ADMIN")

                        // 3. REST API routes - Allow SYSTEM_ADMIN and Tenant ADMINs
                        .requestMatchers("/api/v1/{tenantId}/**").hasAnyRole("SYSTEM_ADMIN", "ADMIN")

                        // 4. Tenant-level routes
                        .requestMatchers("/tenant/**").hasAnyRole("SYSTEM_ADMIN", "ADMIN", "MANAGER", "LEAD", "ASSOCIATE")

                        // 5. Everything else requires a login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Use our custom TenantAuthenticationDetailsSource
                        .authenticationDetailsSource(tenantAuthenticationDetailsSource)
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
