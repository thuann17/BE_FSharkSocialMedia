package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.services.admin.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/post")
public class PostController {
    @Autowired
    private AdminPostService adminPostService;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size
    ) {

        Page<PostDto> posts = adminPostService.getPost(page, size);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(adminPostService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer postId, @RequestBody PostModel postModel) {
        return ResponseEntity.ok(adminPostService.updatePost(postId, postModel));
    }
}
