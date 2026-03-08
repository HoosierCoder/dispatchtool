package com.hoosiercoder.dispatchtool.context;

public class TenantContext {

    public static final String SYSTEM_TENANT = "SYSTEM";
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static void clear() {
        currentTenant.remove();
    }
}
