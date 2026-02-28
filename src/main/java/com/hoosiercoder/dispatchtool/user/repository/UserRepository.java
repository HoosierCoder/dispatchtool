package com.hoosiercoder.dispatchtool.user.repository;

import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Author: HoosierCoder
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByTenantId(String tenantId);
    Optional<User> findByTenantIdAndUserId(String tenantId, Long id);
    List<User> findByTenantIdAndLastName(String tenantId, String lastName);
    List<User> findByTenantIdAndUserRole(String tenantId, UserRole userRole);
    List<User> findByTenantIdAndIsActiveTrue(String tenantId);
    List<User> findByTenantIdAndIsActiveFalse(String tenantId);

    // Legacy/Internal lookups (Filtered by TenantAspect at runtime)
    List<User> findByLastName(String lastName);
    List<User> findByUserRole(UserRole userRole);
    List<User> findByIsActiveTrue();
    List<User> findByIsActiveFalse();
    User findByUserId(Long id);
}
