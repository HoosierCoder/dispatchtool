package com.hoosiercoder.dispatchtool.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TenantAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, TenantAuthenticationDetails> {

    private static final Logger logger = LoggerFactory.getLogger(TenantAuthenticationDetailsSource.class);

    // Regex for paths like /{tenantId}/login, /{tenantId}/logout, /{tenantId}/error
    private static final Pattern ROOT_TENANT_PATH_PATTERN = Pattern.compile("^/([^/]+)/(login|logout|error).*");
    // Regex for paths like /tenant/{tenantId}/...
    private static final Pattern WEB_TENANT_PATH_PATTERN = Pattern.compile("^/tenant/([^/]+)/.*");

    @Override
    public TenantAuthenticationDetails buildDetails(HttpServletRequest context) {
        String tenantId = null;
        String path = context.getRequestURI();

        logger.info("TenantAuthenticationDetailsSource: Building details for request URI: {}", path);

        // 1. Try to get tenantId from request parameters (e.g., hidden field in login form)
        tenantId = context.getParameter("tenantId");
        if (tenantId != null && !tenantId.isEmpty()) {
            logger.info("TenantAuthenticationDetailsSource: Found tenant '{}' from request parameter.", tenantId);
        } else {
            // 2. Fallback to extracting from URI path
            Matcher matcher;

            // Check for paths like /{tenantId}/login, /{tenantId}/logout, /{tenantId}/error
            matcher = ROOT_TENANT_PATH_PATTERN.matcher(path);
            if (matcher.matches()) {
                tenantId = matcher.group(1);
                logger.info("TenantAuthenticationDetailsSource: Found tenant '{}' from ROOT_TENANT_PATH_PATTERN.", tenantId);
            }

            // Check for /tenant/{tenantId}/... (fallback if not a root-level auth path)
            if (tenantId == null) {
                matcher = WEB_TENANT_PATH_PATTERN.matcher(path);
                if (matcher.matches()) {
                    tenantId = matcher.group(1);
                    logger.info("TenantAuthenticationDetailsSource: Found tenant '{}' from WEB_TENANT_PATH_PATTERN.", tenantId);
                }
            }
        }
        
        if (tenantId == null) {
            logger.warn("TenantAuthenticationDetailsSource: No tenant ID found in URI '{}' or request parameters.", path);
        }

        return new TenantAuthenticationDetails(context, tenantId);
    }
}
