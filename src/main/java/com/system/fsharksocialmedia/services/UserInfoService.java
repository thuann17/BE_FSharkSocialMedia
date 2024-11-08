package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
import com.system.fsharksocialmedia.entities.Image;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.models.LoginModel;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.repositories.UserroleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService  {

    @Autowired
    private UserRepository repository;

//    @Autowired
//    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserroleRepository userroleRepository;

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDtoWithImages)
                .collect(Collectors.toList());
    }public UserDto getByUsername(String username) {
        User u = userRepository.findByUsername(username).orElse(null);
        return convertToUserDto(u);
    }

    private UserDto convertToUserDtoWithImages(User user) {
        UserDto userDto = convertToUserDto(user); // Convert to basic UserDto
        if (user.getImages() != null) { // Check if the user has images
            List<ImageDto> imageDtos = user.getImages().stream()
                    .map(this::convertToImageDto) // Convert each Image to ImageDto
                    .collect(Collectors.toList());
            userDto.setImages((Set<ImageDto>) imageDtos); // Set the list of ImageDto in UserDto
        }
        return userDto; // Return the populated UserDto
    }

    public UserDto addUser(LoginModel model) {
        User us = new User();
        us.setUsername(model.getUsername());
        us.setPassword(model.getPassword());
        us.setEmail(model.getEmail());
        us.setActive(Optional.ofNullable(model.getActive()).orElse(true));
        us.setGender(Boolean.valueOf(model.getGender()));
        us.setLastname(model.getLastname());
        us.setFirstname(model.getFirstname());
        us.setBirthday(LocalDate.parse(model.getBirthday()));
        us.setBio(model.getBio());
        us.setHometown(model.getHometown());
        us.setCurrency(model.getCurrency());
        Userrole role = userroleRepository.findById(model.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + model.getRoleId()));
        System.out.println("Role: " + role);
        us.setRoles(role);
        try {
            User savedUser = repository.save(us);
            return convertToUserDto(savedUser);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not commit JPA transaction", e);
        }
    }

    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setLastname(user.getLastname());
        userDto.setFirstname(user.getFirstname());
        userDto.setActive(user.getActive());
        userDto.setBio(user.getBio());
        userDto.setHometown(user.getHometown());
        userDto.setCurrency(user.getCurrency());
        userDto.setGender(user.getGender());
        userDto.setBirthday(user.getBirthday());
        if (user.getRoles() != null) {
            UserroleDto userroleDto = new UserroleDto();
            userroleDto.setId(user.getRoles().getId());
            userroleDto.setRole(user.getRoles().getRole());
            userDto.setRoles(userroleDto);
        }
        return userDto;
    }

    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setImage(image.getImage());
        imageDto.setCreateDate(image.getCreateDate()); // Make sure this method matches the DTO
        imageDto.setAvatarUrl(image.getAvatarUrl()); // Ensure the field name matches in DTO
        imageDto.setCoverUrl(image.getCoverUrl()); // Ensure the field name matches in DTO
        imageDto.setStatus(image.getStatus());

        // Check if the user is not null and set the username
        if (image.getUser() != null) {
            imageDto.setUsername(image.getUser().getUsername()); // Assuming there's a getUsername method in User
        }

        return imageDto;
    }


    public User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setActive(userDto.getActive());
        user.setBio(userDto.getBio());
        user.setHometown(userDto.getHometown());
        user.setCurrency(userDto.getCurrency());
        user.setGender(userDto.getGender());
        user.setBirthday(userDto.getBirthday());
        if (userDto.getRoles() != null) {
            Userrole role = userroleRepository.findById(user.getRoles().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + userDto.getRoles().getId()));
            user.setRoles(role);
        }
        return user;
    }


}
