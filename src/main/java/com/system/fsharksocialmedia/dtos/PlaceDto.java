package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

@Data
public class PlaceDto implements Serializable {
    Integer id;
    @Size(max = 500)
    String nameplace;
    @Size(max = 1000)
    String urlmap;
    @Size(max = 1000)
    String address;
    @Size(max = 1000)
    String description;
    Set<PlaceimageDto> placeimages;
}