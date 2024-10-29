package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class UsertripDto implements Serializable {
    Integer id;
    TripDto tripid;
    UserDto userid;
    TriproleDto role;
}