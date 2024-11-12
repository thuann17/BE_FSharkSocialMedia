package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
import com.system.fsharksocialmedia.entities.Image;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.exceptions.UserNotFoundException;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        dto.setBio(user.getBio());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setLastname(user.getLastname());
        dto.setFirstname(user.getFirstname());
        dto.setBirthday(user.getBirthday());
        dto.setHometown(user.getHometown());
        dto.setCurrency(user.getCurrency());

        if (user.getRoles() != null) {
            UserroleDto userRoleDto = new UserroleDto();
            userRoleDto.setRole(user.getRoles().getRole());
            dto.setRoles(userRoleDto);
        }


        return dto;
    }

    // Convert UserDto to User entity (optional if needed)
    public User convertToEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setActive(dto.getActive());
        user.setBio(dto.getBio());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setLastname(dto.getLastname());
        user.setFirstname(dto.getFirstname());
        user.setBirthday(dto.getBirthday());
        user.setHometown(dto.getHometown());
        user.setCurrency(dto.getCurrency());
        if (dto.getRoles() != null) {
            user.setRoles(convertToEntity(dto.getRoles()));
        }
        return user;
    }


    private Userrole convertToEntity(UserroleDto dto) {
        Userrole userRole = new Userrole();
        userRole.setRole(dto.getRole());
        return userRole;
    }

    private Image convertToEntity(ImageDto dto) {
        Image image = new Image();
        image.setImage(dto.getImage());
        return image;
    }

    public Page<UserDto> getUsers(int page, int size, String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = (search == null || search.isEmpty())
                    ? userRepository.findAll(pageable)
                    : userRepository.findByUsernameContainingIgnoreCase(search, pageable);
            return users.map(this::convertToDto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user list: " + e.getMessage());
        }
    }

    public UserDto updateAccount(String username, UserModel userModel) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(("Không tìm thấy username: " + username)));
        user.setActive(userModel.getActive());
        User saveAccount = userRepository.save(user);
//        messagingTemplate.convertAndSend("/topic/account-status", convertToDto(user));
        convertToDto(user);
        return convertToDto(saveAccount);

    }

    // Delete a user by their ID
    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UserNotFoundException("Không tìm thấy username: " + username);
        }
        userRepository.deleteById(username);
    }

    // Find User by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by username and return DTO
    public UserDto getUserByUsername(String username) {
        Optional<User> userOptional = findByUsername(username);
        return userOptional.map(this::convertToDto).orElse(null);
    }

}
