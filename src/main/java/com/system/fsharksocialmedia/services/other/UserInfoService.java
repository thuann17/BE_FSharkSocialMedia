package com.system.fsharksocialmedia.services.other;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
import com.system.fsharksocialmedia.entities.Image;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.ImageRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.repositories.UserroleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserroleRepository userroleRepository;
    @Autowired
    private ImageRepository imageRepository;

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDtoWithImages)
                .collect(Collectors.toList());
    }

    public UserDto getByUsername(String username) {
        User u = userRepository.findById(username).orElse(null);
        return convertToUserDto(u);
    }

    private UserDto convertToUserDtoWithImages(User user) {
        UserDto userDto = convertToUserDto(user);
        List<ImageDto> imageDtos = (user.getImages() != null) ? user.getImages().stream()
                .map(this::convertToImageDto)
                .collect(Collectors.toList()) : new ArrayList<>();
        userDto.setImages(imageDtos);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.findById(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public UserDto addUser(UserModel model) {
      Optional<User> existingUser = userRepository.findById(model.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User data or Username is missing");
        }
        if (model.getUsername() == null || model.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (model.getPassword() == null || model.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (model.getEmail() == null || model.getEmail().isEmpty() || !model.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (model.getLastname() == null || model.getLastname().isEmpty()) {
            throw new IllegalArgumentException("Lastname cannot be empty");
        }
        if (model.getFirstname() == null || model.getFirstname().isEmpty()) {
            throw new IllegalArgumentException("Firstname cannot be empty");
        }
        if (model.getBirthday() == null) {
            throw new IllegalArgumentException("Birthday cannot be null");
        }
        if (model.getGender() == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        User us = new User();
        us.setUsername(model.getUsername());
        us.setPassword(encoder.encode(model.getPassword()));
        us.setEmail(model.getEmail());
        us.setActive(true);
        us.setGender(true);
        us.setLastname(model.getLastname());
        us.setFirstname(model.getFirstname());
        us.setBirthday(model.getBirthday());
        us.setBio(model.getBio());
        us.setHometown(model.getHometown());
        us.setCurrency(model.getCurrency());
        Userrole role = userroleRepository.findById(2)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + model.getRoleId()));
        us.setRoles(role);
        String avatarUrl = model.getAvatarUrl() != null ? model.getAvatarUrl() :
                "https://firebasestorage.googleapis.com/v0/b/socialmedia-8bff2.appspot.com/o/ThuanImage%2Favt.jpg?alt=media";
        Image image = new Image();
        image.setAvatarrurl(avatarUrl);
        image.setUsername(us);
        image.setStatus(true);
        image.setCreatedate(Instant.now());
        Set<Image> imageSet = new HashSet<>();
        imageSet.add(image);
        us.setImages(imageSet);
        try {
            User savedUser = repository.save(us);
            imageRepository.save(image);
            return convertToUserDto(savedUser);
        } catch (Exception e) {
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
        if (user.getImages() != null && !user.getImages().isEmpty()) {
            List<ImageDto> imageDtos = new ArrayList<>();
            for (Image image : user.getImages()) {
                ImageDto imageDto = convertToImageDto(image);
                imageDtos.add(imageDto);
            }
            userDto.setImages(imageDtos);
        }
        return userDto;
    }

    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();

        if (image != null) {
            imageDto.setId(image.getId());
            imageDto.setImage(image.getImage());
            imageDto.setCreatedate(image.getCreatedate());
            imageDto.setAvatarrurl(image.getAvatarrurl());
            imageDto.setCoverurl(image.getCoverurl());
            imageDto.setStatus(image.getStatus());
            if (image.getUsername() != null) {
                imageDto.setUsername(convertToUserDto(image.getUsername()));
            }
        }

        return imageDto;
    }

}
