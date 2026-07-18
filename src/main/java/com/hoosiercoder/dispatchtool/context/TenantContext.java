package com.hoosiercoder.dispatchtool.context;

public class TenantContext {

    public static final String SYSTEM_TENANT = "SYSTEM";
    // Change to InheritableThreadLocal to propagate context to child threads
    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

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
