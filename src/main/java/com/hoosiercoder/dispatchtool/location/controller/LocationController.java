package com.hoosiercoder.dispatchtool.location.controller;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: HoosierCoder
 *
 */
@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDto) {
        // ID is usually null for new creations; the service handles tenant assignment
        LocationDTO createdLocation = locationService.createLocation(locationDto);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> listLocations() {
        // Changed from getAllLocations to listLocations for consistency
        List<LocationDTO> locations = locationService.listLocations();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        // Service handles the tenant-scoping safety check
        return ResponseEntity.ok(locationService.getLocationById(id));
    }
}
