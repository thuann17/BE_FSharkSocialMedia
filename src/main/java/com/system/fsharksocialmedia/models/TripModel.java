package com.system.fsharksocialmedia.models;

import com.system.fsharksocialmedia.dtos.PlacetripDto;
import com.system.fsharksocialmedia.dtos.TripDto;
import com.system.fsharksocialmedia.dtos.TriproleDto;
import com.system.fsharksocialmedia.dtos.UsertripDto;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripModel {

    private UsertripDto usertripDto;
    private PlacetripDto placetripDto;
    private TripDto tripDto;


}
