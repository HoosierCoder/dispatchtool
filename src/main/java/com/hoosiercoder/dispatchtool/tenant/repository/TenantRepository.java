package com.hoosiercoder.dispatchtool.tenant.repository;

import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: HoosierCoder
 *
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
    // You might want to check if a company name is already taken
    Optional<Tenant> findByCompanyName(String companyName);
}
