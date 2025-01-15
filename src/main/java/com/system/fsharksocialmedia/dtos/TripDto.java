package com.system.fsharksocialmedia.dtos;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
public class TripDto implements Serializable {
    Integer tripid;
    String tripname;
    Instant startdate;
    Instant enddate;
    Instant createdate;
    String description;
    Set<PlacetripDto> placetrips;
    Set<UsertripDto> usertrips;
}