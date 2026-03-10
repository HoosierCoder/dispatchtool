package com.hoosiercoder.dispatchtool.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class DispatchUserDetails extends User {

    private final String tenantId;
    private final String firstName;
    private final String lastName;

    public DispatchUserDetails(String username, String password, boolean enabled,
                               boolean accountNonExpired, boolean credentialsNonExpired,
                               boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                               String tenantId, String firstName, String lastName) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenantId = tenantId;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
