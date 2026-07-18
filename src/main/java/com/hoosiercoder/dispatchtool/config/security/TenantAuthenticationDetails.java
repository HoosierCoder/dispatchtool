package com.hoosiercoder.dispatchtool.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class TenantAuthenticationDetails extends WebAuthenticationDetails {

    private final String tenantId;

    public TenantAuthenticationDetails(HttpServletRequest request, String tenantId) {
        super(request);
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    @Override
    public String toString() {
        return super.toString() + "; TenantId: " + this.getTenantId();
    }
}
