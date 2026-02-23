package com.hoosiercoder.dispatchtool.location.mapper;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import org.mapstruct.Mapper;

/**
 * Author: HoosierCoder
 *
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location locationDtoToLocation(LocationDTO locationDTO);

    LocationDTO locationToLocationDto(Location location);
}
