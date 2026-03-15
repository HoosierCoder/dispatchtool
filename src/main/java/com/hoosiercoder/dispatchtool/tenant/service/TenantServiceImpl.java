package com.hoosiercoder.dispatchtool.tenant.service;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.tenant.mapper.TenantMapper;
import com.hoosiercoder.dispatchtool.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Override
    @Transactional
    public TenantDTO createTenant(TenantDTO tenantDto) {
        Tenant tenant = tenantMapper.tenantDtoToTenant(tenantDto);
        // Force lowercase ID for URL consistency
        tenant.setTenantId(tenantDto.getTenantId().toLowerCase().trim());

        Tenant saved = tenantRepository.save(tenant);
        return tenantMapper.tenantToTenantDto(saved);
    }

    @Override
    @Transactional
    public TenantDTO updateTenant(TenantDTO tenantDto) {
        Tenant tenant = tenantMapper.tenantDtoToTenant(tenantDto);
        // Force lowercase ID for consistency
        if (tenant.getTenantId() != null) {
            tenant.setTenantId(tenant.getTenantId().toLowerCase().trim());
        }
        Tenant saved = tenantRepository.save(tenant);
        return tenantMapper.tenantToTenantDto(saved);
    }

    @Override
    @Transactional
    public void toggleTenantStatus(String tenantId) {
        String normalizedId = tenantId.toLowerCase().trim();
        Tenant tenant = tenantRepository.findById(normalizedId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant Id:" + normalizedId));
        tenant.setActive(!tenant.isActive());
        tenantRepository.save(tenant);
    }

    @Override
    public List<TenantDTO> listAllTenants() {
        return tenantRepository.findAll().stream()
                .map(tenantMapper::tenantToTenantDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TenantDTO> getTenantById(String tenantId) {
        return tenantRepository.findById(tenantId.toLowerCase().trim())
                .map(tenantMapper::tenantToTenantDto);
    }

    @Override
    @Transactional
    public void deleteTenant(String tenantId) {
        tenantRepository.deleteById(tenantId.toLowerCase().trim());
    }
}
