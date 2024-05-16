package com.example.registrationms.controller;

import com.example.registrationms.dto.SubjectDTO;
import com.example.registrationms.model.Subject;
import com.example.registrationms.respository.SubjectRepository;
import com.example.registrationms.service.TeachingRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectRepository subjectRepo;
    private final TeachingRegisterService teachingRegisterService;

    @GetMapping("/all")
    public ResponseEntity<List<Subject>> subjects() {
        return ResponseEntity.ok(subjectRepo.findAll());
    }

    @GetMapping("/{subjectCode}")
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable String subjectCode) {
        return ResponseEntity.ok(teachingRegisterService.getSubject(subjectCode));
    }
}
