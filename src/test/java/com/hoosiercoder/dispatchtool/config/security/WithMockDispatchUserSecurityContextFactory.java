package com.hoosiercoder.dispatchtool.config.security;

import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockDispatchUserSecurityContextFactory implements WithSecurityContextFactory<WithMockDispatchUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockDispatchUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        DispatchUserDetails principal = new DispatchUserDetails(
                customUser.username(),
                "password",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + customUser.role())),
                customUser.tenantId(),
                "Test",
                "User",
                "Test Tenant Corp",
                UserRole.ADMIN
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
