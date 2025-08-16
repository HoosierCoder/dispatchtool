package com.hoosiercoder.dispatchtool.user.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Author: HoosierCoder
 */
public enum UserRole {
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
        String[] names = {"ASSOCIATE"
                        ,"LEAD"
                        ,"MANAGER"
                        ,"ADMIN"};
        return Arrays.asList(names);
    }

}
