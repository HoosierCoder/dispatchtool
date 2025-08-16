package com.hoosiercoder.dispatchtool.user.service;

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
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO saveNewUser(UserDTO userDTO) {
        User newUser = userMapper.userDtoToUser(userDTO);
        User user = userRepository.save(newUser);
        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDTO> listUsers() {

        return userRepository.findAll()
                .stream().map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<UserDTO> getUserByLastName(String lastName) {
        if( lastName == null ) {
            return List.of();
        }

        return userRepository.findByLastName(lastName).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUserByUserRole(UserRole userRole) {

        return userRepository.findByUserRole(userRole).stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getByIsActive() {
        return userRepository.findByIsActiveTrue().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}
