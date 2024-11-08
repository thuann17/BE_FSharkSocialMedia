package com.system.fsharksocialmedia.controllers;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDto> getPostsWithUserDetails(@RequestParam String username) {
        return postService.getPostsWithUserDetails(username);
    }
}
