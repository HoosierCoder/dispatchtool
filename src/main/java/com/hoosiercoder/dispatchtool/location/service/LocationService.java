package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    LocationDTO createLocation(LocationDTO locationDto);
    LocationDTO getLocationById(Long id);
    List<LocationDTO> listLocations();
}
