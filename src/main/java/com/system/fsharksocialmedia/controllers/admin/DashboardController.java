package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.services.admin.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("")
    public PostDto getPostCountByYearAndMonth(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        return dashboardService.getPostAndTripCountByYearAndMonth(year, month);
    }

    @GetMapping("/top")
    public List<Map<String, Object>> getPostCount(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        return dashboardService.getUserPostCount(year, month);
    }

    @GetMapping("/popular")
    public List<Map<String, Object>> getPostCountTop5(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        return dashboardService.getUserPostCountTop5(year, month);
    }
}
