package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant; // Import Tenant entity
import com.hoosiercoder.dispatchtool.tenant.repository.TenantRepository; // Import TenantRepository
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TenantRepository tenantRepository; // Inject TenantRepository

    public CustomUserDetailsService(UserRepository userRepository, TenantRepository tenantRepository) { // Add TenantRepository to constructor
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);

        String tenantIdToUseForLookup = null;
        String usernameForParsing = username; // Use this for parsing, can be reassigned
        String usernameForDbLookup; // This will be the effectively final username for DB lookup

        // 1. Try to get tenantId from Authentication Details (set by TenantAuthenticationDetailsSource)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof TenantAuthenticationDetails) {
            TenantAuthenticationDetails details = (TenantAuthenticationDetails) authentication.getDetails();
            tenantIdToUseForLookup = details.getTenantId();
            logger.info("Tenant ID retrieved from TenantAuthenticationDetails: '{}'", tenantIdToUseForLookup);
        } else {
            logger.warn("TenantAuthenticationDetails not found in SecurityContext for user '{}'. Falling back to username parsing or assuming system user.", usernameForParsing);
        }

        // 2. If not found in details, try to get tenantId from username@tenant format
        if (tenantIdToUseForLookup == null && usernameForParsing.contains("@")) {
            final String[] parts = usernameForParsing.split("@", 2);
            usernameForDbLookup = parts[0]; // This is the username part for DB lookup
            String tenantIdFromUsername = parts[1];
            logger.info("Parsed tenant user from username. Username: {}, tenantId: {}", usernameForDbLookup, tenantIdFromUsername);
            tenantIdToUseForLookup = tenantIdFromUsername;
        } else {
            usernameForDbLookup = usernameForParsing; // No parsing needed, use original username
        }


        // 3. If tenantId is still null or SYSTEM_TENANT, it's a system user or an error for tenant user
        if (tenantIdToUseForLookup == null || TenantContext.SYSTEM_TENANT.equals(tenantIdToUseForLookup)) {
            // This path is for system users, or if a tenant user somehow got here without tenantId
            logger.info("Attempting to load user '{}' as a system user.", usernameForDbLookup);
            User user = userRepository.findByUsername(usernameForDbLookup) // Use usernameForDbLookup
                    .orElseThrow(() -> {
                        logger.error("System user not found: {}", usernameForDbLookup); // Use usernameForDbLookup in lambda
                        return new UsernameNotFoundException("System user not found: " + usernameForDbLookup);
                    });
            TenantContext.setTenantId(TenantContext.SYSTEM_TENANT); // Ensure context is set
            logger.info("System user found: {}. Role: {}. Storing TenantContext: '{}'", user.getUsername(), user.getUserRole(), TenantContext.SYSTEM_TENANT);
            return new DispatchUserDetails(
                    user.getUsername(),
                    user.getHashedPassword(),
                    user.isActive(),
                    true, true, true,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())),
                    TenantContext.SYSTEM_TENANT,
                    user.getFirstName(),
                    user.getLastName(),
                    "System Admin", // Default tenantName for system admin
                    user.getUserRole() // Pass userRole
            );
        } else {
            // 4. Load as a tenant user using the resolved tenantId
            return loadUserByUsernameAndTenantId(usernameForDbLookup, tenantIdToUseForLookup); // Use usernameForDbLookup
        }
    }

    // This method is specifically for loading tenant users with an explicit tenantId
    public UserDetails loadUserByUsernameAndTenantId(String username, String tenantId) throws UsernameNotFoundException {
        logger.info("Attempting to load user '{}' for tenant '{}'", username, tenantId);

        if (tenantId == null || tenantId.isEmpty() || TenantContext.SYSTEM_TENANT.equals(tenantId)) {
            logger.error("Invalid tenantId '{}' provided for user '{}'.", tenantId, username);
            throw new UsernameNotFoundException("Invalid tenant context for user: " + username);
        }

        User user = userRepository.findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> {
                    logger.error("User '{}' not found for tenant '{}'", username, tenantId);
                    return new UsernameNotFoundException("User not found: " + username + " for tenant: " + tenantId);
                });

        // Fetch tenant details to get the company name
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> {
                    logger.error("Tenant '{}' not found for user '{}'.", tenantId, username);
                    return new UsernameNotFoundException("Tenant not found for user: " + username);
                });
        String tenantName = tenant.getCompanyName();


        // Set the context for the rest of the request
        TenantContext.setTenantId(tenantId);

        logger.info("User found: {}. Role: {}. Storing TenantContext: '{}'", user.getUsername(), user.getUserRole(), tenantId);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name());

        return new DispatchUserDetails(
                user.getUsername(),
                user.getHashedPassword(),
                user.isActive(),
                true,
                true,
                true,
                Collections.singletonList(authority),
                tenantId, // Passing the correct tenant ID here
                user.getFirstName(),
                user.getLastName(),
                tenantName, // Pass the tenantName here
                user.getUserRole() // Pass userRole
        );
    }
}
