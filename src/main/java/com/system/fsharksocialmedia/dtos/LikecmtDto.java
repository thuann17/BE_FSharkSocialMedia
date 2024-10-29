package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class LikecmtDto implements Serializable {
    Integer id;
    UserDto username;
    CommentDto comment;
}