package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<LocationDTO> findAllByTenant();
    LocationDTO createLocation(LocationDTO locationDto);
    List<LocationDTO> listLocations();
    Optional<LocationDTO> getLocationById(Long id);
    LocationDTO updateLocation(Long id, LocationDTO locationDto);
    void deleteLocation(Long id);
}
