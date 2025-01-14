package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
public class PostDto implements Serializable {
    Integer id;
    UserDto username;
    @Size(max = 200)
    String content;
    Instant createdate;
    Boolean status;
    Set<CommentDto> comments;
    Set<LikepostDto> likeposts;
    Set<NotificationDto> notifications;
    Set<PostimageDto> postimages;
    Set<ShareDto> shares;
    Long countLike;
    Long countComment;
    boolean isLikedByUser;
    Integer countPost;
    Integer tripCount;
}