package com.example.registrationms.controller;

import com.example.registrationms.dto.SubjectDTO;
import com.example.registrationms.model.Subject;
import com.example.registrationms.respository.SubjectRepository;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SubjectControllerTest {
    @Autowired
    private TeachingRegisterService registerService;

    @Autowired
    private SubjectRepository subjectRepo;

    @Test
    void subjects() {
        var result = subjectRepo.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("SQA");
        assertThat(result.get(1).getCode()).isEqualTo("SQA");
    }

    @Test
    @Transactional
    void getCourse() {
        var result = registerService.getSubject("SQA");
        assertThat(result).isInstanceOf(SubjectDTO.class);
        assertThat(result.getName()).isEqualTo("Quản lý chất lượng phần mềm");
    }

    @Test
    void getSubjects(){
        assertThat(true).isEqualTo(true);
    }
}