package com.hoosiercoder.dispatchtool.tenant.mapper;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import org.mapstruct.Mapper;

/**
 * Author: HoosierCoder
 *
 */
@Mapper(componentModel = "spring")
public interface TenantMapper {
    TenantDTO tenantToTenantDto(Tenant tenant);
    Tenant tenantDtoToTenant(TenantDTO tenantDto);
}
