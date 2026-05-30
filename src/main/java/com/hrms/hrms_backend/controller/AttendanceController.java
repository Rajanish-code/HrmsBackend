package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.domain.AttendanceLog;
import com.hrms.hrms_backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    
    @PostMapping("/clock-in")
    public ResponseEntity<Map<String, Object>> clockIn(@RequestBody Map<String, Long> request) {
        return ResponseEntity.ok(attendanceService.clockIn(request.get("workerId"), request.get("siteId")));
    }

    
    @PostMapping("/clock-out")
    public ResponseEntity<AttendanceLog> clockOut(@RequestBody Map<String, Long> request) {
        return ResponseEntity.ok(attendanceService.clockOut(request.get("workerId")));
    }

   
    @GetMapping("/log")
    public ResponseEntity<Page<AttendanceLog>> getLogs(
            @RequestParam Long workerId,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(attendanceService.getAttendanceHistory(workerId, from, to, page, size));
    }
} 