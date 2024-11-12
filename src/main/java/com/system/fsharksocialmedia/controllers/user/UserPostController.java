package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.services.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user/post")
public class UserPostController {
    @Autowired
    private UserPostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostModel postModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(postModel));
    }

    @PutMapping("/{postID}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer postID, @RequestBody PostModel postModel) {
        return ResponseEntity.ok(postService.updatePost(postID,postModel));
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postID) {
        postService.deletePost(postID);
        return ResponseEntity.noContent().build();
    }
}
