package com.hoosiercoder.dispatchtool.config.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockDispatchUserSecurityContextFactory.class)
public @interface WithMockDispatchUser {
    String username() default "testuser";
    String tenantId() default "test-tenant";
    String role() default "SYSTEM_ADMIN";
}
