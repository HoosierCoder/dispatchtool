package com.hoosiercoder.dispatchtool.location.repository;

import com.hoosiercoder.dispatchtool.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByTenantId(String tenantId);

    // Used in CustomerServiceImpl to verify the billing address belongs to the tenant
    Optional<Location> findByTenantIdAndId(String tenantId, Long id);
}
