package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AboutService {

    @Autowired
    UserRepository userRepository;

    public Map<String, Object> getUserPersonalInfo(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Lấy URL ảnh đại diện (avatar)
            String avatarUrl = user.getImages().stream()
                    .filter(image -> image.getStatus() != null && image.getStatus()) // Lọc ảnh có trạng thái chính
                    .map(image -> image.getAvatarrurl())
                    .findFirst()
                    .orElse("default-avatar-url"); // URL mặc định nếu không có ảnh đại diện

            // Trả về dữ liệu người dùng dưới dạng Map
            return Map.of(
                    "username", user.getUsername(),
                    "firstname", user.getFirstname(),
                    "lastname", user.getLastname(),
                    "email", user.getEmail(),
                    "gender", user.getGender(),
                    "birthday", user.getBirthday(),
                    "bio", user.getBio(),
                    "hometown", user.getHometown(),
                    "avatarUrl", avatarUrl
            );
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }

}
