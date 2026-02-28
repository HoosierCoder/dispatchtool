package com.hoosiercoder.dispatchtool.tenant.service;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.tenant.mapper.TenantMapper;
import com.hoosiercoder.dispatchtool.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;

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
    public TenantDTO createTenant(TenantDTO tenantDto) {
        Tenant tenant = tenantMapper.tenantDtoToTenant(tenantDto);
        // Force lowercase ID for URL consistency
        tenant.setTenantId(tenantDto.getTenantId().toLowerCase().trim());

        Tenant saved = tenantRepository.save(tenant);
        return tenantMapper.tenantToTenantDto(saved);
    }

    @Override
    public List<TenantDTO> listAllTenants() {
        return tenantRepository.findAll().stream()
                .map(tenantMapper::tenantToTenantDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TenantDTO> getTenantById(String tenantId) {
        return tenantRepository.findById(tenantId)
                .map(tenantMapper::tenantToTenantDto);
    }

    @Override
    public void deleteTenant(String tenantId) {
        tenantRepository.deleteById(tenantId);
    }
}
