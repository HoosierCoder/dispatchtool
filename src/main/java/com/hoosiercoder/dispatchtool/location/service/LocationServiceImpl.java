package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationDTO> findAllByTenant() {
        String tenantId = TenantContext.getTenantId();
        return locationRepository.findByTenantId(tenantId).stream()
                .map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDto) {
        String tenantId = TenantContext.getTenantId();
        Location location = locationMapper.locationDtoToLocation(locationDto);
        location.setTenantId(tenantId);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.locationToLocationDto(savedLocation);
    }

    @Override
    public List<LocationDTO> listLocations() {
        String tenantId = TenantContext.getTenantId();
        return locationRepository.findByTenantId(tenantId).stream()
                .map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LocationDTO> getLocationById(Long id) {
        String tenantId = TenantContext.getTenantId();
        return locationRepository.findByTenantIdAndId(tenantId, id)
                .map(locationMapper::locationToLocationDto);
    }
}
