package com.hoosiercoder.dispatchtool.user.service;

import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Author: HoosierCoder
 */
public interface UserService {
    UserDTO saveNewUser(UserDTO userDTO);

    List<UserDTO> listUsers();

    Optional<UserDTO> getUserById(Long id);

    List<UserDTO> getUserByLastName(String lastName);

    List<UserDTO> getUserByUserRole(UserRole userRole);

    List<UserDTO> getByIsActive();
}
