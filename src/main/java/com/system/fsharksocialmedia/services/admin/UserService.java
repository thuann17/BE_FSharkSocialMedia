package com.system.fsharksocialmedia.services.admin;

import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Convert entity to DTO
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setLastname(user.getLastname());
        dto.setFirstname(user.getFirstname());
        dto.setBirthday(user.getBirthday());
        dto.setBio(user.getBio());
        dto.setHometown(user.getHometown());
        dto.setCurrency(user.getCurrency());
        return dto;
    }

    // Convert DTO to entity
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setActive(dto.getActive());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setLastname(dto.getLastname());
        user.setFirstname(dto.getFirstname());
        user.setBirthday(dto.getBirthday());
        user.setBio(dto.getBio());
        user.setHometown(dto.getHometown());
        user.setCurrency(dto.getCurrency());
        return user;
    }

    // Find User by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }

    // Get user by username and return DTO
    public UserDto getUserByUsername(String username) {
        Optional<User> userOptional = findByUsername(username);
        return userOptional.map(this::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersWithoutFriends(String username) {
        return userRepository.findUsersWithoutFriends(username);
    }


}
