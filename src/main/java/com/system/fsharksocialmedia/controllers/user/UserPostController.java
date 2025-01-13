package com.system.fsharksocialmedia.controllers.user;


import com.system.fsharksocialmedia.dtos.CommentDto;
import com.system.fsharksocialmedia.dtos.LikepostDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.ShareDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.models.CommentModel;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.models.ShareModel;
import com.system.fsharksocialmedia.services.user.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.system.fsharksocialmedia.services.user.PostService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/post")
public class UserPostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserPostService uPostService;


    @GetMapping("/getPostByListFriends")
    public ResponseEntity<List<PostDto>> getPost(@RequestParam String username) {
        return ResponseEntity.ok().body(uPostService.getPostsByFriends(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getPostsWithUserDetails(@PathVariable String username) {
        try {
            List<PostDto> posts = postService.getPostsWithUserDetails(username);
            if (posts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/shares/{username}")
    public ResponseEntity<List<ShareDto>> getSharesByUsername(@PathVariable String username) {
        List<ShareDto> shares = uPostService.getSharesByUsername(username);
        if (shares.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(shares);
    }

    @PostMapping("/{username}")
    public ResponseEntity<PostDto> createPost(@PathVariable String username, @RequestBody PostModel postModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(uPostService.addPost(username, postModel));
    }

    @PostMapping("share/add/{username}/{postId}")
    public ResponseEntity<ShareDto> addShare(
            @PathVariable String username, @PathVariable Integer postId,
            @RequestBody ShareModel model) {

        try {
            // Call the service method to add a new share
            ShareDto shareDto = uPostService.addShare(username,postId, model);
            return ResponseEntity.ok(shareDto); // Return the created ShareDto with 200 OK status
        } catch (RuntimeException e) {
            // In case of error (like user not found or post not found)
            return ResponseEntity.status(400).body(null); // Return 400 Bad Request
        }
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

    @DeleteMapping("deleteShare/{shareId}")
    public ResponseEntity<String> deleteShare(@PathVariable Integer shareId) {
        try {
            uPostService.deleteShare(shareId);  // Call the delete method from service
            return ResponseEntity.ok("Bài chia sẻ đã bị xóa!");  // Return success message
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());  // Return error if not found
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<?> getCountLikesForPost(@PathVariable Integer postId) {
        try {
            Long likeCount = uPostService.countLikesForPost(postId);
            return ResponseEntity.ok(likeCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/likes/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Integer postId, @RequestParam String username) {
        try {
            // Gọi service để thêm like
            LikepostDto likepostDto = uPostService.likePost(username, postId);
            return ResponseEntity.ok(likepostDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/has-liked/{postId}")
    public ResponseEntity<Boolean> hasLikedPost(@PathVariable Integer postId, @RequestParam String username) {
        boolean hasLiked = uPostService.hasUserLikedPost(username, postId);
        return ResponseEntity.ok(hasLiked);
    }

    @DeleteMapping("/likes/{postId}")
    public ResponseEntity<Map<String, String>> removeLike(@PathVariable Integer postId, @RequestParam String username) {
        try {
            String message = uPostService.removeLike(postId, username);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error while unliking the post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Integer postId) {
        List<CommentDto> comments = uPostService.getCommentsByPostId(postId);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/comments/{postId}")
    public ResponseEntity<?> commentPost(@PathVariable Integer postId, @RequestParam String username, @RequestBody CommentModel model) {
        try {
            CommentDto commentDto = uPostService.addComment(postId, username, model);
            return ResponseEntity.ok(commentDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/likecomment/{commentId}")
    public ResponseEntity<Integer> getLikesByCommentId(@PathVariable Integer commentId) {
        int likeCount = uPostService.getLikesByCommentId(commentId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/checkLike/{commentId}/{username}")
    public ResponseEntity<Boolean> checkLike(@PathVariable Integer commentId, @PathVariable String username) {
        try {
            // Kiểm tra xem người dùng đã like bình luận chưa
            boolean isLiked = uPostService.isLikedByUser(commentId, username);
            return ResponseEntity.ok(isLiked);
        } catch (RuntimeException e) {
            // Nếu không tìm thấy người dùng, trả về lỗi
            return ResponseEntity.status(404).body(false);
        }
    }

    @DeleteMapping("deletecmt/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Integer cmtId) {
        try {
            uPostService.deleteComment(cmtId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }

}
