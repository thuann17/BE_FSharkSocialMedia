package com.system.fsharksocialmedia.dtos;

import com.system.fsharksocialmedia.entities.Trip;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

@Data
public class PlacetripDto implements Serializable {
    Integer id;
    PlaceDto placeid;
    Trip tripid;
    Instant datetime;
    @Size(max = 500)
    String note;
}