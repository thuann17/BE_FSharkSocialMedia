package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.PlacetripDto;
import com.system.fsharksocialmedia.dtos.TripDto;
import com.system.fsharksocialmedia.models.PlaceTripModel;
import com.system.fsharksocialmedia.services.user.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/trip")
public class TripController {
    @Autowired
    private TripService tripService;

    @GetMapping("/tripplace/{username}")
    public ResponseEntity<List<TripDto>> getTripsByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(tripService.getTripsByUsername(username));
    }

    @PostMapping("/{username}")
    public ResponseEntity<PlacetripDto> createTrip(
            @PathVariable String username,
            @RequestBody PlaceTripModel placeTripModel
    ) {
        try {
            PlacetripDto placetripDto = tripService.createTrip(username, placeTripModel.getPlaceId(), placeTripModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(placetripDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/{tripId}")
    public ResponseEntity<TripDto> updateTrip(
            @PathVariable Integer tripId, @RequestBody PlaceTripModel model
    ) {
        return ResponseEntity.ok().body(tripService.updateTrip(tripId, model));
    }

    @DeleteMapping("/delete/{tripId}")
    public ResponseEntity<String> deleteTrip(@PathVariable Integer tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/start-dates")
    public List<TripDto> getTripStartDates() {
        return tripService.getTripStartDates();
    }
}
