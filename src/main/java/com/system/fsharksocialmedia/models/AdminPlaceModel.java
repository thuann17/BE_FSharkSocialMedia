package com.system.fsharksocialmedia.models;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
@Data
public class AdminPlaceModel {
    @Size(max = 500)
    String namePlace;
    @Size(max = 1000)
    String urlMap;
    @Size(max = 1000)
    String address;
    @Size(max = 1000)
    String description;
    @Size(min = 1, max = 10)
    private Set<String> placeImages;
}
