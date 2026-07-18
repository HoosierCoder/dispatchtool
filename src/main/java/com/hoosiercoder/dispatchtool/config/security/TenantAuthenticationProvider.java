package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.context.TenantContext; // Keep import for clarity if needed elsewhere
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import this
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component; // REMOVED

public class TenantAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TenantAuthenticationProvider(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // CustomUserDetailsService.loadUserByUsername now handles tenantId resolution
        // from TenantAuthenticationDetails or from username@tenant format.
        // It will throw UsernameNotFoundException if tenant context is missing or user not found.
        UserDetails userDetails;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Re-throw as BadCredentialsException for security reasons (don't reveal if user exists)
            throw new BadCredentialsException("Invalid username or password", e);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Ensure the TenantContext is set for the current thread after successful authentication
        // This is important for subsequent operations within the same request
        if (userDetails instanceof DispatchUserDetails) {
            TenantContext.setTenantId(((DispatchUserDetails) userDetails).getTenantId());
        } else {
            // Fallback for system users if they don't use DispatchUserDetails
            TenantContext.setTenantId(TenantContext.SYSTEM_TENANT);
        }


        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
