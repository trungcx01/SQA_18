package com.example.registrationms.controller;

import com.example.registrationms.model.Classroom;
import com.example.registrationms.respository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomRepository classroomRepo;
    @GetMapping("/all")
    public ResponseEntity<List<Classroom>> getClassrooms() {
        return ResponseEntity.ok(classroomRepo.findAll());
    }
}
