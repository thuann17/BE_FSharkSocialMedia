package com.system.fsharksocialmedia.services.admin;

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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (user.getImages() != null) {
            List<ImageDto> imageDtos = user.getImages().stream()
                    .map(this::convertToImageDto)
                    .collect(Collectors.toList());
            dto.setImages(imageDtos);
        }
        return dto;
    }

    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setImage(image.getImage());
        imageDto.setCreatedate(image.getCreatedate());
        imageDto.setAvatarrurl(image.getAvatarrurl());
        imageDto.setCoverurl(image.getCoverurl());
        imageDto.setStatus(image.getStatus());
        return imageDto;
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

    public Page<UserDto> getUserAccount(int page, int size, String search, Boolean active, String role) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users;
            if (search != null && !search.isEmpty()) {
                if (active != null && role != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndActiveAndRoles_Role(search, active, role, pageable);
                } else if (active != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndActive(search, active, pageable);
                } else if (role != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndRoles_Role(search, role, pageable);
                } else {
                    users = userRepository.findByUsernameContainingIgnoreCase(search, pageable);
                }
            } else {
                if (active != null && role != null) {
                    users = userRepository.findByActiveAndRoles_Role(active, role, pageable);
                } else if (active != null) {
                    users = userRepository.findByActive(active, pageable);
                } else if (role != null) {
                    users = userRepository.findByRoles_Role(role, pageable);
                } else {
                    users = userRepository.findAll(pageable);
                }
            }

            return users.map(this::convertToDto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user list: " + e.getMessage());
        }
    }

    public Page<UserDto> getAdminAccount(int page, int size, String search, Boolean active, String role, String excludedUsername) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users;

            if (search != null && !search.isEmpty()) {
                if (active != null && role != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndActiveAndRoles_RoleAndUsernameNot(search, active, role, excludedUsername, pageable);
                } else if (active != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndActiveAndUsernameNot(search, active, excludedUsername, pageable);
                } else if (role != null) {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndRoles_RoleAndUsernameNot(search, role, excludedUsername, pageable);
                } else {
                    users = userRepository.findByUsernameContainingIgnoreCaseAndUsernameNot(search, excludedUsername, pageable);
                }
            } else {
                if (active != null && role != null) {
                    users = userRepository.findByActiveAndRoles_RoleAndUsernameNot(active, role, excludedUsername, pageable);
                } else if (active != null) {
                    users = userRepository.findByActiveAndUsernameNot(active, excludedUsername, pageable);
                } else if (role != null) {
                    users = userRepository.findByRoles_RoleAndUsernameNot(role, excludedUsername, pageable);
                } else {
                    users = userRepository.findAllByUsernameNot(excludedUsername, pageable);
                }
            }

            return users.map(this::convertToDto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user list: " + e.getMessage());
        }
    }



    public UserDto updateAccount(String username, UserModel userModel) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(("Không tìm thấy username: " + username)));
        user.setActive(userModel.getActive());
        User saveAccount = userRepository.save(user);
        messagingTemplate.convertAndSend("/topic/account-status", convertToDto(user));
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
        return userRepository.findById(username);
    }

    public UserDto getUserByUsername(String username) {
        Optional<User> userOptional = findByUsername(username);
        return userOptional.map(this::convertToDto).orElse(null);
    }

    public UserDto lockUnlockAccount(String username, String password) throws Exception {
        // Tìm người dùng trong cơ sở dữ liệu
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new Exception("User not found");
        }

        // Kiểm tra mật khẩu với bcrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Incorrect password");
        }

        // Đảo ngược trạng thái active của tài khoản
        user.setActive(!user.getActive());

        // Lưu thay đổi vào cơ sở dữ liệu
        user = userRepository.save(user);

        return convertToDto(user);
    }

}
