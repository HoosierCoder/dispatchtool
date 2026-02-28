package com.hoosiercoder.dispatchtool.location.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldFindLocationOnlyByTenantId() {
        // Arrange
        String tenantId = "dispatch-pro-99";
        Long locationId = 500L;
        TenantContext.setTenantId(tenantId);

        Location location = new Location();
        location.setId(locationId);
        location.setTenantId(tenantId);

        LocationDTO dto = new LocationDTO();
        dto.setId(locationId);

        when(locationRepository.findByTenantIdAndId(tenantId, locationId))
                .thenReturn(Optional.of(location));
        when(locationMapper.locationToLocationDto(location)).thenReturn(dto);

        // Act
        LocationDTO result = locationService.getLocationById(locationId);

        // Assert
        assertEquals(locationId, result.getId());
        verify(locationRepository).findByTenantIdAndId(tenantId, locationId);
    }
}
