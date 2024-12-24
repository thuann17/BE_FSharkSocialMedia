package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.CommentDto;
import com.system.fsharksocialmedia.dtos.LikepostDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.ShareDto;
import com.system.fsharksocialmedia.entities.Likepost;
import com.system.fsharksocialmedia.models.InteractModel;
import com.system.fsharksocialmedia.services.InteractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class InteractController {
    @Autowired
    private InteractService interactService;

    @PostMapping("/like")
    public ResponseEntity<LikepostDto> likePost(@RequestBody InteractModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interactService.likePost(model));
    }

    @DeleteMapping("/like/{idLike}")
    public ResponseEntity<Void> deleteLikePost(@PathVariable Integer idLike) {
        boolean isDeleted = interactService.deleteLikePost(idLike);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDto> addCommentPost(@RequestBody InteractModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interactService.commentPost(model));
    }

    @PutMapping("/comment/{commentID}")
    public ResponseEntity<CommentDto> updateCommentPost(@PathVariable Integer commentID, @RequestBody InteractModel model) {
        return ResponseEntity.ok(interactService.updateComment(commentID, model));
    }

    @DeleteMapping("/comment/{commentID}")
    public ResponseEntity<Void> deleteCommentPost(@PathVariable Integer commentID) {
        boolean isDeleted = interactService.deleteCommentPost(commentID);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // share
    @PostMapping("/share")
    public ResponseEntity<ShareDto> sharePost(@RequestBody InteractModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interactService.sharePost(model));
    }

    @PutMapping("/share/{shareID}")
    public ResponseEntity<ShareDto> updateSharePost(@PathVariable Integer shareID, @RequestBody InteractModel model) {
        return ResponseEntity.ok(interactService.updateSharePost(shareID, model));
    }

    @DeleteMapping("/share/{shareID}")
    public ResponseEntity<Void> deleteSharePost(@PathVariable Integer shareID) {
        boolean isDeleted = interactService.deleteSharePost(shareID);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
