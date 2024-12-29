package com.system.fsharksocialmedia.controllers.user;


import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.services.user.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.system.fsharksocialmedia.services.user.PostService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/post")
public class UserPostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserPostService uPostService;

    @GetMapping
    public List<PostDto> getPostsWithUserDetails(@RequestParam String username) {
        return postService.getPostsWithUserDetails(username);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostModel postModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(uPostService.addPost(postModel));
    }

    @PutMapping("/{postID}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer postID, @RequestBody PostModel postModel) {
        return ResponseEntity.ok(uPostService.updatePost(postID, postModel));
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postID) {
        uPostService.deletePost(postID);
        return ResponseEntity.noContent().build();

    }
}
