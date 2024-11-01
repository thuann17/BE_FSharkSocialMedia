package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.services.AdminProfileByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/profile")
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
}
