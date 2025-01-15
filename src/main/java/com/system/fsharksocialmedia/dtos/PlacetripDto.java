package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class PlacetripDto implements Serializable {
    Integer id;
    PlaceDto placeid;
    TripDto tripid;
    Instant datetime;
    @Size(max = 500)
    String note;
}