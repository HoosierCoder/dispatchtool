package com.hoosiercoder.dispatchtool.user.mapper;

import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Author: HoosierCoder
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    // When mapping from UserDTO to User entity (for creation)
    @Mapping(target = "hashedPassword", ignore = true) // Password should be handled by service layer for security
    @Mapping(target = "tenant", ignore = true) // Tenant is set by service/filter, not directly from DTO
    @Mapping(source = "username", target = "username") // Explicitly map username for new user creation
    User userDtoToUser(UserDTO userDTO);

    // When mapping from User entity to UserDTO
    @Mapping(source = "tenant.companyName", target = "tenantName")
    @Mapping(source = "tenantId", target = "tenantId")
    UserDTO userToUserDto(User user);

    // For updating an existing user entity from a DTO
    @Mapping(target = "userId", ignore = true) // ID should not be updated from DTO
    @Mapping(target = "hashedPassword", ignore = true) // Password handled by service
    @Mapping(target = "tenantId", ignore = true) // Tenant should not change on update
    @Mapping(target = "tenant", ignore = true) // Tenant should not change on update
    @Mapping(target = "username", ignore = true) // Username is typically immutable, not updated via DTO
    void updateUserFromDto(UserDTO userDTO, @MappingTarget User user);
}
