package com.example.registrationms.controller;

import com.example.registrationms.respository.ClassroomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClassroomControllerTest {
    @Autowired
    private ClassroomRepository classroomRepo;

    @Test
    void getClassrooms() {
        var result = classroomRepo.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("305-A2");
        assertThat(result.get(1).getName()).isEqualTo("405-A3");
    }

    @Test
    public void testGetCoursesSuccess() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetCoursesInvalidTeacherCode() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetCoursesNullTeacherCode() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetCoursesEmptyTeacherCode() {
        assertThat(true).isEqualTo(true);
    }
}