package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDto) {
        String tenantId = TenantContext.getTenantId();
        Location location = locationMapper.locationDtoToLocation(locationDto);

        // Ensure the new location is tagged with the current tenant
        location.setTenantId(tenantId);

        Location savedLocation = locationRepository.save(location);
        return locationMapper.locationToLocationDto(savedLocation);
    }

    @Override
    public List<LocationDTO> listLocations() {
        String tenantId = TenantContext.getTenantId();

        // Use the tenant-specific repository method
        return locationRepository.findByTenantId(tenantId)
                .stream()
                .map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDTO getLocationById(Long id) {
        String tenantId = TenantContext.getTenantId();

        // Use findByTenantIdAndId to prevent cross-tenant access
        Location location = locationRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Location not found or access denied"));

        return locationMapper.locationToLocationDto(location);
    }
}
