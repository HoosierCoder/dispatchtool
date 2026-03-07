package com.hoosiercoder.dispatchtool.user.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Author: HoosierCoder
 */
public enum UserRole {
    SYSTEM_ADMIN("System Administrator"), // The Root/Owner of Dispatch
    ASSOCIATE("Field Associate"),
    LEAD("Lead Associate"),
    MANAGER("Manager"),
    ADMIN("Admin");

    private final String role;

    UserRole(final String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static List<String> getRoleNames() {
        String[] names = {"SYSTEM_ADMIN"
                        ,"ASSOCIATE"
                        ,"LEAD"
                        ,"MANAGER"
                        ,"ADMIN"};
        return Arrays.asList(names);
    }

    public static List<UserRole> getTenantVisibleRoles() {
        return Arrays.stream(UserRole.values())
                .filter(role -> role != SYSTEM_ADMIN)
                .toList();
    }

}
