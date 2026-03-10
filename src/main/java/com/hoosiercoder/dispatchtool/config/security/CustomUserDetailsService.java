package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);

        final Optional<User> userOptional;
        String tenantIdForContext;

        if (username.contains("@")) {
            // Tenant-level user: "user@tenant"
            final String[] parts = username.split("@", 2);
            String actualUsername = parts[0];
            String tenantId = parts[1];
            logger.info("Parsed tenant user. Username: {}, tenantId: {}", actualUsername, tenantId);
            userOptional = userRepository.findByTenantIdAndUsername(tenantId, actualUsername);
            tenantIdForContext = tenantId;
        } else {
            // System-level user (e.g., SYSTEM_ADMIN)
            logger.info("Parsed system user. Username: {}", username);
            userOptional = userRepository.findByUsername(username);
            tenantIdForContext = TenantContext.SYSTEM_TENANT;
        }

        User user = userOptional.orElseThrow(() -> {
            logger.error("User not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username);
        });

        // Set the context for the rest of the request (useful for the initial login request)
        TenantContext.setTenantId(tenantIdForContext);
        logger.info("User found: {}. Setting TenantContext to: {}", user.getUsername(), tenantIdForContext);

        // Spring expects roles to be prefixed with "ROLE_"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name());

        // Return our custom UserDetails containing the tenantId
        return new DispatchUserDetails(
                user.getUsername(),
                user.getHashedPassword(),
                user.isActive(),
                true,
                true,
                true,
                Collections.singletonList(authority),
                tenantIdForContext,
                user.getFirstName(),
                user.getLastName()
        );
    }
}
