package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class UsertripDto implements Serializable {
    Integer id;
    TripDto tripid;
    UserDto userid;
    TriproleDto role;
    @Size(max = 50)
    String status;
}