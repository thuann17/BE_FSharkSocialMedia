package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.PlacetripDto;
import com.system.fsharksocialmedia.dtos.TripDetailsDto;
import com.system.fsharksocialmedia.dtos.TripDto;
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
    @PostMapping(value = "/addTripDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTripDetails(@RequestBody TripModel tripDetailsWrapperDto) {
        try {
            tripService.addTripDetails(tripDetailsWrapperDto);
            return ResponseEntity.ok("Trip details added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding trip details: " + e.getMessage());
        }
    }
    // 2. Gửi yêu cầu tham gia chuyến đi
    @PostMapping("/request-join")
    public String requestJoinTrip(@RequestParam Integer tripId, @RequestParam String username) {
        return tripService.requestJoinTrip(tripId, username);
    }
    @PostMapping("/approve-request")
    public String approveOrRejectRequest(@RequestParam Integer userTripId, @RequestParam boolean isApproved) {
        return tripService.approveOrRejectRequest(userTripId, isApproved);
    }

    // 4. Ghi lại điểm đến trong chuyến đi
    @PostMapping("/record-place")
    public PlacetripDto recordPlace(@RequestParam Integer tripId, @RequestParam Integer placeId,
                                    @RequestParam String datetime, @RequestParam String note) {
        Instant date = Instant.parse(datetime); // Chuyển đổi ngày từ chuỗi
        return tripService.recordPlace(tripId, placeId, date, note);
    }

    // 5. Lấy danh sách chuyến đi của người dùng
//    @GetMapping("/details/{username}")
//    public ResponseEntity<TripDetailsDto> getTripDetailsByUsername(@PathVariable String username) {
//        try {
//            TripDetailsDto tripDetailsDto = tripService.getTripDetailsByUsername(username);
//            System.out.println("hello");
//            return new ResponseEntity<>(tripDetailsDto, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            System.out.println("lỗi"+e.getMessage());
//
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    // 6. Đánh dấu chuyến đi là hoàn thành
    @PostMapping("/complete")
    public TripDto completeTrip(@RequestParam Integer tripId) {
        return tripService.completeTrip(tripId);
    }


}
