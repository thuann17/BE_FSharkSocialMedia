package com.system.fsharksocialmedia.dtos;

import com.system.fsharksocialmedia.entities.Likecmt;
import com.system.fsharksocialmedia.entities.Likepost;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto implements Serializable {
    private String username;
    private UserroleDto roles;
    private String email;
    private String password;
    private Boolean active;
    private Boolean gender;
    private String lastname;
    private String firstname;
    private LocalDate birthday;
    private String bio;
    private String hometown;
    private String currency;

    private Set<ImageDto> images; // Assuming you have a corresponding ImageDto
    private Set<CommentDto> comments; // Assuming you have a corresponding CommentDto
    private Set<GroupmemberDto> groupmembers; // Assuming you have a corresponding GroupmemberDto
    private Set<NotificationDto> notifications; // Assuming you have a corresponding NotificationDto
    private Set<PostDto> posts; // Assuming you have a corresponding PostDto
    private Set<ShareDto> shares; // Assuming you have a corresponding ShareDto
    private Set<MessageDto> messages; // Assuming you have a corresponding MessageDto
    private Set<Likepost> likeposts; // Assuming you have a corresponding LikePostDto
    private Set<Likecmt> likecmts; // Assuming you have a corresponding LikeCmtDto
    private Set<UsertripDto> usertrips;
}