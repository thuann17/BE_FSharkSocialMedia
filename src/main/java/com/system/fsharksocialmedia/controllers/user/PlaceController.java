package com.system.fsharksocialmedia.controllers.user;


import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.services.user.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;


    @GetMapping
    public ResponseEntity<List<PlaceDto>> getPlaceDetailsWithImages(
            @RequestParam(value = "addressFilter", required = false) String addressFilter) {
        List<PlaceDto> places = placeService.getPlaceDetailsWithImages(addressFilter);
        return ResponseEntity.ok(places);
    }
}