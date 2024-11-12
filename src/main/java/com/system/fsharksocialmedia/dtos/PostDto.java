package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostDto implements Serializable {
    Integer id;

    UserDto username;

    @Size(max = 200)
    String content;

    Instant createdate;

    Boolean status;

    Integer commentCount;

    Integer likeCount;
    Set<String> images;
    Set<CommentDto> comments;
    Set<LikepostDto> likeposts;
    Set<NotificationDto> notifications;
    Set<PostimageDto> postimages;
    Set<ShareDto> shares;
    Long countLike;
    Long countComment;
}