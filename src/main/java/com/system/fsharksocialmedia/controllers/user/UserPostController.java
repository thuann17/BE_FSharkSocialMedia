package com.system.fsharksocialmedia.controllers.user;


import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/posts")
public class UserPostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDto> getPostsWithUserDetails(@RequestParam String username) {
        return postService.getPostsWithUserDetails(username);
    }
}
