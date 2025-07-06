package com.hoosiercoder.dispatchtool.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
}
