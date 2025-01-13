package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.models.AdminPlaceModel;
import com.system.fsharksocialmedia.services.admin.AdminPlaceService;
import com.system.fsharksocialmedia.services.user.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/place")
public class AdminPlaceController {

    @Autowired
    private AdminPlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getPlace() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PostMapping
    public ResponseEntity<PlaceDto> createPlace(@RequestBody AdminPlaceModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.addPlace(model));
    }
}
