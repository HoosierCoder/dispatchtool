package com.hoosiercoder.dispatchtool.tenant.repository;

import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
}
