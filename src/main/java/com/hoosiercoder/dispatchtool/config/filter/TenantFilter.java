package com.hoosiercoder.dispatchtool.config.filter;

import com.hoosiercoder.dispatchtool.config.security.DispatchUserDetails;
import com.hoosiercoder.dispatchtool.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: HoosierCoder
 *
 * Restores the TenantContext from the authenticated user's session or the URL.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    // Regex for paths like /{tenantId}/login, /{tenantId}/logout, /{tenantId}/error
    private static final Pattern ROOT_TENANT_PATH_PATTERN = Pattern.compile("^/([^/]+)/(login|logout|error).*");
    // Regex for paths like /tenant/{tenantId}/...
    private static final Pattern WEB_TENANT_PATH_PATTERN = Pattern.compile("^/tenant/([^/]+)/.*");
    // Regex for paths like /api/v1/{tenantId}/...
    private static final Pattern API_TENANT_PATH_PATTERN = Pattern.compile("^/api/v1/([^/]+)/.*");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String tenantId = null;
            String path = request.getRequestURI();

            // 1. Try to get tenant from the authenticated user's session (for web UI after login)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) { // Ensure user is actually authenticated
                if (authentication.getPrincipal() instanceof DispatchUserDetails) {
                    DispatchUserDetails userDetails = (DispatchUserDetails) authentication.getPrincipal();
                    tenantId = userDetails.getTenantId();
                    System.out.println("DEBUG FILTER: Found tenant in session: " + tenantId);
                } else if (authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal())) {
                    // Ignore anonymousUser principal, it's not a real login
                } else {
                    System.out.println("DEBUG FILTER: Principal is NOT DispatchUserDetails: " + authentication.getPrincipal().getClass().getName());
                }
            }

            // 2. If not found in session, try to get it from the URL
            if (tenantId == null) {
                Matcher matcher;

                // Check for paths like /{tenantId}/login, /{tenantId}/logout, /{tenantId}/error
                matcher = ROOT_TENANT_PATH_PATTERN.matcher(path);
                if (matcher.matches()) {
                    tenantId = matcher.group(1);
                    System.out.println("DEBUG FILTER: Found tenant in root-level auth URL: " + tenantId);
                }

                // Check for /tenant/{tenantId}/...
                if (tenantId == null) { // Only check if not already found
                    matcher = WEB_TENANT_PATH_PATTERN.matcher(path);
                    if (matcher.matches()) {
                        tenantId = matcher.group(1);
                        System.out.println("DEBUG FILTER: Found tenant in /tenant/ web URL: " + tenantId);
                    }
                }

                // Check for /api/v1/{tenantId}/...
                if (tenantId == null) { // Only check if not already found
                    matcher = API_TENANT_PATH_PATTERN.matcher(path);
                    if (matcher.matches()) {
                        tenantId = matcher.group(1);
                        System.out.println("DEBUG FILTER: Found tenant in API URL: " + tenantId);
                    }
                }
            }
            
            if (tenantId != null) {
                TenantContext.setTenantId(tenantId);
                // Add this debug line to confirm TenantContext is set right before filterChain.doFilter
                System.out.println("DEBUG FILTER: TenantContext set to '" + TenantContext.getTenantId() + "' before proceeding with filter chain.");
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
