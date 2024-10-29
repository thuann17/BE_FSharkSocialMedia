package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class PlaceimageDto implements Serializable {
    Integer id;
    PlaceDto placeid;
    String image;
}