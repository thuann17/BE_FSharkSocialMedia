package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.dtos.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripModel {
    String tripName;
    LocalDate startDate;
    LocalDate endDate;
    Instant createDate = Instant.now();
    String description;
}
