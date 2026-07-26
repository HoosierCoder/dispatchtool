package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.user.entity.UserRole; // Import UserRole
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class DispatchUserDetails extends User {

    private final String tenantId;
    private final String firstName;
    private final String lastName;
    private final String tenantName;
    private final UserRole userRole; // Added userRole field

    public DispatchUserDetails(String username, String password, boolean enabled,
                               boolean accountNonExpired, boolean credentialsNonExpired,
                               boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                               String tenantId, String firstName, String lastName, String tenantName,
                               UserRole userRole) { // Added userRole to constructor
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenantId = tenantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tenantName = tenantName;
        this.userRole = userRole; // Initialize userRole
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public UserRole getUserRole() { // Added getter for userRole
        return userRole;
    }
}
