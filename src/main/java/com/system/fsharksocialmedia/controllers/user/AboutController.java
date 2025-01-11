package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.admin.UserService;
import com.system.fsharksocialmedia.services.user.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/about")
public class AboutController {

    @Autowired
    AboutService aboutService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/info/{username}")
    public ResponseEntity<Object> getUserPersonalInfo(@PathVariable String username) {
        var userInfo = aboutService.getUserPersonalInfo(username);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "User not found",
                            "username", username
                    ));
        }
    }
}


