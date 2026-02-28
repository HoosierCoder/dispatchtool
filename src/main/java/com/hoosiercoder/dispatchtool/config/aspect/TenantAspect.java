package com.hoosiercoder.dispatchtool.config.aspect;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: HoosierCoder
 *
 */
@Aspect
@Component
public class TenantAspect {

    @Autowired
    private EntityManager entityManager;

    // This "Pointcut" tells Spring to run this before any method in your repository package
    @Before("execution(* com.hoosiercoder.dispatchtool.*.repository.*.*(..))")
    public void beforeRepositoryMethod() {
        String tenantId = TenantContext.getTenantId(); //

        if (tenantId != null) {
            // Unwrapping the Hibernate Session from the EntityManager
            Session session = entityManager.unwrap(Session.class);
            // Enabling the filter we defined in the Entity
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        }
    }
}
