package com.system.fsharksocialmedia.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class FriendDto implements Serializable {
    Integer id;
    UserDto userTarget;
    UserDto userSrc;
    Instant createdate;
    Boolean status;
    String friendName;
    String friendUserName;
    String friendAvatar;
}