package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Author: HoosierCoder
 *
 */
@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationDTO locationDto;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId(1L);
        location.setStreetAddress("123 Main St");

        locationDto = new LocationDTO();
        locationDto.setId(1L);
        locationDto.setStreetAddress("123 Main St");
    }

    @Test
    void createLocation_ShouldReturnSavedLocationDTO() {

        when(locationMapper.locationDtoToLocation(any(LocationDTO.class))).thenReturn(location);
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.locationToLocationDto(any(Location.class))).thenReturn(locationDto);

        LocationDTO result = locationService.createLocation(locationDto);

        // Assert
        assertNotNull(result);
        assertEquals("123 Main St", result.getStreetAddress());
        verify(locationRepository, times(1)).save(any());
    }

    @Test
    void getLocationById_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> locationService.getLocationById(1L));
    }
}
