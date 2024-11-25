package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.TripDto;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/trip")
public class TripController {
    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripDto> addTrip(@RequestBody TripModel model) {
        return ResponseEntity.ok(tripService.createTrip(model));
    }
}
