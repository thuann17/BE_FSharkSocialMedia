package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.dtos.*;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripModel {
    private TripDto trip;
    private List<UsertripDto> userTrips;
    private List<PlacetripDto> placeTrips;

}
