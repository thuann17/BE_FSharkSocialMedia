package com.system.fsharksocialmedia.controllers;

import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")  // Cho phép các request từ nguồn frontend cụ thể
@RequestMapping("/api/place")  // Định nghĩa URL chính cho controller
public class PlaceController {

    @Autowired
    private PlaceService placesService;

    // API để lấy danh sách các địa điểm có hỗ trợ phân trang và tìm kiếm
    @GetMapping
    public ResponseEntity<Page<PlaceDto>> getPlaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Page<PlaceDto> places = placesService.getPlaces(page, size, search);  // Gọi phương thức từ service
        return ResponseEntity.ok(places);  // Trả về kết quả dưới dạng JSON
    }
}
