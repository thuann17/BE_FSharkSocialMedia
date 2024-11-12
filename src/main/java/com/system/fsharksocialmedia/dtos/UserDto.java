package com.system.fsharksocialmedia.dtos;

import com.system.fsharksocialmedia.entities.Likecmt;
import com.system.fsharksocialmedia.entities.Likepost;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDto implements Serializable {

    @Size(max = 200)
    String username;
    UserroleDto roles;
    @Size(max = 255)
    String password;
    Boolean active;
    @Size(max = 200)
    String email;
    Boolean gender;
    @Size(max = 100)
    String lastname;
    @Size(max = 100)
    String firstname;
    LocalDate birthday;
    @Size(max = 500)
    String bio;
    @Size(max = 200)
    String hometown;
    @Size(max = 100)
    String currency;
    private List<ImageDto> images;
    private Set<CommentDto> comments;
    private Set<GroupmemberDto> groupmembers;
    private Set<NotificationDto> notifications;
    private Set<PostDto> posts;
    private Set<ShareDto> shares;
    private Set<MessageDto> messages;
    private Set<Likepost> likeposts;
    private Set<Likecmt> likecmts;
    private Set<UsertripDto> usertrips;

    public UserDto(String username) {
        this.username = username;
    }

}