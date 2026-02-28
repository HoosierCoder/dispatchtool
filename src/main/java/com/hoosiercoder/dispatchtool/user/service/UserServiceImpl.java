package com.hoosiercoder.dispatchtool.user.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import com.hoosiercoder.dispatchtool.user.mapper.UserMapper;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO saveNewUser(UserDTO userDTO) {
        String tenantId = TenantContext.getTenantId();
        User newUser = userMapper.userDtoToUser(userDTO);

        // Ensure the new user is locked to the current tenant
        newUser.setTenantId(tenantId);

        User user = userRepository.save(newUser);
        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDTO> listUsers() {
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByTenantId(tenantId)
                .stream().map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByTenantIdAndUserId(tenantId, id)
                .map(userMapper::userToUserDto);
    }

    @Override
    public List<UserDTO> getUserByLastName(String lastName) {
        if (lastName == null) {
            return List.of();
        }
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByTenantIdAndLastName(tenantId, lastName).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUserByUserRole(UserRole userRole) {
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByTenantIdAndUserRole(tenantId, userRole).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getByIsActive() {
        String tenantId = TenantContext.getTenantId();
        return userRepository.findByTenantIdAndIsActiveTrue(tenantId).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}
