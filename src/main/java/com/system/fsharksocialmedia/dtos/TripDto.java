package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class TripDto implements Serializable {
    Integer id;
    @Size(max = 500)
    String tripname;
    LocalDate startdate;
    LocalDate enddate;
    Instant createdate;
    String description;
}