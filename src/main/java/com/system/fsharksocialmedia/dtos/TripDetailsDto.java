package com.system.fsharksocialmedia.dtos;


import lombok.Data;

import java.util.List;

@Data
public class TripDetailsDto {
    private TripDto trip;
    private List<UsertripDto> userTrips;
    private List<PlacetripDto> placetrips;
}
