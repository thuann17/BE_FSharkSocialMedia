package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.models.PasswordModel;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.services.admin.AdminProfileByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/profile")
public class AdminProfileByUserController {
    @Autowired
    private AdminProfileByUserService adminProfileByUserService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminProfileByUserService.getUserByUsername(username));
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminProfileByUserService.getPostByUsername(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String username, @RequestBody UserModel model) {
        return ResponseEntity.ok().body(adminProfileByUserService.updateProfile(username, model));
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable String username,
            @RequestBody PasswordModel model) {
        return ResponseEntity.ok().body(adminProfileByUserService.updatePassword(username, model));
    }

    @PutMapping("/{username}/uImage")
    public ResponseEntity<UserDto> updateImage(@PathVariable String username, @RequestBody UserModel avatarUrl) {
        return ResponseEntity.ok().body(adminProfileByUserService.updateImage(username, avatarUrl));
    }
}
