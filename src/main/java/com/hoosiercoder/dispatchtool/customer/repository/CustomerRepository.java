package com.hoosiercoder.dispatchtool.customer.repository;

import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 1. The "Safe" way to get everything for a company
    List<Customer> findByTenantId(String tenantId);

    // 2. The "Safe" way to get a specific record
    Optional<Customer> findByTenantIdAndId(String tenantId, Long id);

    // 3. SHADOWING: Redefine the default findAll to make it stand out
    @Deprecated
    @Override
    default List<Customer> findAll() {
        throw new UnsupportedOperationException("Direct findAll() is disabled for Multi-Tenancy. Use findByTenantId().");
    }
}