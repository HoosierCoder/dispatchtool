package com.hoosiercoder.dispatchtool.tenant.service;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;

import java.util.List;
import java.util.Optional;

/**
 * Author: HoosierCoder
 *
 */
public interface TenantService {
    TenantDTO createTenant(TenantDTO tenantDto);
    List<TenantDTO> listAllTenants();
    Optional<TenantDTO> getTenantById(String tenantId);
    void deleteTenant(String tenantId);
}
