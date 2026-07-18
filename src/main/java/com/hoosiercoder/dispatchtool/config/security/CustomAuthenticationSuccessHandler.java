package com.hoosiercoder.dispatchtool.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Author: HoosierCoder
 *
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String targetUrl = "/"; // Default fallback

        if (authentication.getPrincipal() instanceof DispatchUserDetails) {
            DispatchUserDetails userDetails = (DispatchUserDetails) authentication.getPrincipal();
            String tenantId = userDetails.getTenantId();

            if (roles.contains("ROLE_SYSTEM_ADMIN")) {
                targetUrl = "/system/dashboard"; // System admin has a different dashboard
            } else if (tenantId != null && !tenantId.isEmpty()) {
                // For tenant users, redirect to their tenant-specific dashboard
                targetUrl = "/tenant/" + tenantId + "/dashboard";
            } else {
                // Fallback for authenticated users without a clear tenantId (shouldn't happen for tenant users)
                targetUrl = "/";
            }
        } else {
            // Handle cases where principal is not DispatchUserDetails (e.g., anonymous, or other custom types)
            if (roles.contains("ROLE_SYSTEM_ADMIN")) {
                targetUrl = "/system/dashboard";
            } else {
                targetUrl = "/"; // Generic redirect
            }
        }

        response.sendRedirect(targetUrl);
    }
}
