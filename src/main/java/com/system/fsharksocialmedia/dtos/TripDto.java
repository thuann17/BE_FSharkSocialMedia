package com.system.fsharksocialmedia.dtos;

import com.system.fsharksocialmedia.entities.Placetrip;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripDto implements Serializable {
    Integer id;
    @Size(max = 500)
    String tripname;
    Instant startdate;
    Instant enddate;
    Instant createdate;
    String description;
    private List<UserDto> users;
    private List<PlacetripDto> placeTrips;
    private List<PlaceDto> places;
}