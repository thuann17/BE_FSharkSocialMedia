package com.system.fsharksocialmedia.models;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class PlaceTripModel {
    int placeId;
    String tripName;
    Instant startDate;
    Instant endDate;
    Instant createDate;
    String description;
    String note;
    Instant datetime;
}
