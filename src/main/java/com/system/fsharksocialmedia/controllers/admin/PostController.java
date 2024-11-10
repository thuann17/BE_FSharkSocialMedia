package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(required = false) String search) {
        Page<PostDto> posts = postService.getPost(page, size, search);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(PostModel postModel, @PathVariable Integer postId) {
        return ResponseEntity.ok(postService.updatePost(postModel, postId));
    }
}