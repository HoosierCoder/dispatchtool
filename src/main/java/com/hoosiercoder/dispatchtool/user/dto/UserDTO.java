package com.hoosiercoder.dispatchtool.user.dto;

import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Author: HoosierCoder
 */
public class UserDTO {

    private Long userId;

    @NotBlank(message = "Username cannot be empty") // Added validation for username
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username; // Added username field

    @NotBlank(message = "Firstname cannot be empty")
    @Size(min = 2, max = 20, message = "Firstname must be between 2 and 20 characters")
    private String firstName;

    @NotBlank(message = "Lastname cannot be empty")
    @Size(min = 2, max = 20, message = "Lastname must be between 2 and 20 characters")
    private String lastName;

    private UserRole userRole;

    private boolean isActive;

    private String tenantId;
    private String tenantName;

    public UserDTO(String username, String firstName, String lastName, UserRole userRole, boolean isActive, String tenantId) {
        this.username = username; // Initialize username
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.isActive = isActive;
        this.tenantId = tenantId;
    }

    public UserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userRole=" + userRole +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
