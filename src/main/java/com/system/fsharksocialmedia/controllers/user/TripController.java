package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.PlacetripDto;
import com.system.fsharksocialmedia.dtos.TripDetailsDto;
import com.system.fsharksocialmedia.dtos.TripDto;
import com.system.fsharksocialmedia.entities.Trip;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.services.user.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/trip")
public class TripController {
    @Autowired
    private TripService tripService;

    //1. Thêm chuyến đi
//    @PostMapping(value = "/addTripDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> addTripDetails(@RequestBody TripModel tripDetailsWrapperDto) {
//        try {
//            tripService.addTripDetails(tripDetailsWrapperDto);
//            return ResponseEntity.ok("Trip details added successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding trip details: " + e.getMessage());
//        }
//    }


    // 5. Lấy danh sách chuyến đi của người dùng
    @GetMapping("/tripplace/{username}")
    public ResponseEntity<List<TripDto>> getTripsByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(tripService.getTripsByUsername(username));
    }



}
