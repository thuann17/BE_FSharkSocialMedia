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
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        Boolean statusBoolean = null;
        if (status != null) {
            if ("true".equalsIgnoreCase(status)) {
                statusBoolean = true;
            } else if ("false".equalsIgnoreCase(status)) {
                statusBoolean = false;
            }
        }
        Page<PostDto> posts = adminPostService.getPost(page, size, search, statusBoolean);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(adminPostService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(PostModel postModel, @PathVariable Integer postId) {
        return ResponseEntity.ok(adminPostService.updatePost(postModel, postId));
    }
}
