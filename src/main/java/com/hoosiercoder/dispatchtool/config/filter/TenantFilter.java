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
 * Restores the TenantContext from the authenticated user's session.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof DispatchUserDetails) {
                DispatchUserDetails userDetails = (DispatchUserDetails) authentication.getPrincipal();
                String tenantId = userDetails.getTenantId();

                if (tenantId != null) {
                    TenantContext.setTenantId(tenantId);
                }
            } else {
                // Fallback for API calls using the Header (if not using session)
                String headerTenantId = request.getHeader("X-Tenant-ID");
                if (headerTenantId != null && !headerTenantId.isEmpty()) {
                    TenantContext.setTenantId(headerTenantId);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
