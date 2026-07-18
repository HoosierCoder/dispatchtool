package com.hoosiercoder.dispatchtool.location.mapper;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Author: HoosierCoder
 *
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "tenantId", ignore = true) // Set by service layer
    Location locationDtoToLocation(LocationDTO locationDTO);

    LocationDTO locationToLocationDto(Location location);
}
