package com.example.registrationms.controller;

import com.example.registrationms.dto.ScheduleDTO;
import com.example.registrationms.service.TeachingRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final TeachingRegisterService teachingRegisterService;
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable int scheduleId) {
        return ResponseEntity.ok(teachingRegisterService.getSchedule(scheduleId));
    }
}
