package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;

@Data
public class ImageDto implements Serializable {
    Integer id;
    String image;
    Instant createdate;
    String avatarrurl;
    String coverurl;
    Boolean status;
    UserDto username;
}
