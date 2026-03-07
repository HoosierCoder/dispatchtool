package com.hoosiercoder.dispatchtool.user.mapper;

import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Author: HoosierCoder
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDTO userDTO);

    @Mapping(source = "tenant.companyName", target = "tenantName")
    UserDTO userToUserDto(User user);
}
