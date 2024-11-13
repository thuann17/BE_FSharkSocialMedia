package com.system.fsharksocialmedia.models;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class TripModel {
    String tripName;
    LocalDate startDate;
    LocalDate endDate;
    Instant createDate;
    String description;
}
