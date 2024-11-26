package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.PlacetripDto;
import com.system.fsharksocialmedia.dtos.TripDto;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/user/trip")
public class TripController {
    @Autowired
    private TripService tripService;
    @PostMapping("/create")
    public TripDto createTrip(@RequestBody TripModel tripModel, @RequestParam String username) {
        return tripService.createTrip(tripModel, username);
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
    @GetMapping("/user-trips")
    public List<TripDto> getTripsByUser(@RequestParam String username) {
        return tripService.getTripsByUser(username);
    }

    // 6. Đánh dấu chuyến đi là hoàn thành
    @PostMapping("/complete")
    public TripDto completeTrip(@RequestParam Integer tripId) {
        return tripService.completeTrip(tripId);
    }
}
