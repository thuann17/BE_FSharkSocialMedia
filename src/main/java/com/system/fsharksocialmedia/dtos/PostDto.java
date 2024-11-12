package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< Updated upstream
=======
import lombok.Value;
>>>>>>> Stashed changes

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
<<<<<<< Updated upstream

@Data
@NoArgsConstructor
@AllArgsConstructor
=======
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
>>>>>>> Stashed changes

public class PostDto implements Serializable {
    Integer id;

    UserDto username;

    @Size(max = 200)
    String content;

    Instant createdate;

    Boolean status;

<<<<<<< Updated upstream
    Integer commentCount;

    Integer likeCount;

    List<String> images;
=======
    Set<CommentDto> comments;

    Set<LikepostDto> likeposts;

    Set<NotificationDto> notifications;

    List<PostimageDto> postimages;

    Set<ShareDto> shares;

    Long countLike;

    Long countComment;


>>>>>>> Stashed changes
}