package com.hoosiercoder.dispatchtool.tenant.repository;

import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findByTenantId(String tenantId);
}
