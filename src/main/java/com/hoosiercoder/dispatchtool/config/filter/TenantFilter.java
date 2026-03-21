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

/**
 * Author: HoosierCoder
 *
 * Restores the TenantContext from the authenticated user's session or the URL.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String tenantId = null;

            // 1. Try to get tenant from the authenticated user's session (for web UI)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                if (authentication.getPrincipal() instanceof DispatchUserDetails) {
                    DispatchUserDetails userDetails = (DispatchUserDetails) authentication.getPrincipal();
                    tenantId = userDetails.getTenantId();
                    System.out.println("DEBUG FILTER: Found tenant in session: " + tenantId);
                } else {
                    System.out.println("DEBUG FILTER: Principal is NOT DispatchUserDetails: " + authentication.getPrincipal().getClass().getName());
                }
            }

            // 2. If not found, try to get it from the URL (for API calls)
            if (tenantId == null) {
                String path = request.getRequestURI();
                if (path.startsWith("/api/v1/")) {
                    String[] parts = path.split("/");
                    if (parts.length > 3) {
                        tenantId = parts[3];
                        System.out.println("DEBUG FILTER: Found tenant in URL: " + tenantId);
                    }
                }
            }
            
            if (tenantId != null) {
                TenantContext.setTenantId(tenantId);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
