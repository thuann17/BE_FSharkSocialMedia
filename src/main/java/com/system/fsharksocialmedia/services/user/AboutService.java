package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.entities.Image;
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

    public Optional<Map<String, Object>> getUserPersonalInfo(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    String avatarUrl = user.getImages().stream()
                            .map(Image::getAvatarrurl)
                            .filter(url -> url != null && !url.isEmpty())
                            .reduce((first, second) -> second)
                            .orElse("default-avatar-url");
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
                });
    }


}
