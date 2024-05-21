package com.example.registrationms.controller;

import com.example.registrationms.model.Course;
import com.example.registrationms.model.Role;
import com.example.registrationms.model.Teacher;
import com.example.registrationms.respository.CourseRepository;
import com.example.registrationms.respository.SubjectRepository;
import com.example.registrationms.respository.TeacherRepository;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseControllerTest {

    private Teacher teacher;
    private Course course, course2;

    @Autowired
    private TeachingRegisterService teachingRegisterService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeAll
    public void setUpBeforeClass() throws Exception {
        teacher = Teacher.builder()
                .dob(LocalDate.of(1995, 12, 29))
                .address("HaNoi")
                .build();
        teacher.setCode("MGV002");
        teacher.setName("Phạm Quốc Việt");
        teacher.setUsername("MGV002");
        teacher.setPassword(encoder.encode("123456"));
        teacher.setRole(Role.TEACHER);
        teacherRepository.save(teacher);

        course = Course.builder()
                .subject(subjectRepository.findById("SQA").orElse(null))
                .teacher(teacher)
                .build();
        course2 = Course.builder()
                .subject(subjectRepository.findById("OOP").orElse(null))
                .teacher(teacher)
                .build();
        courseRepository.save(course);
        courseRepository.save(course2);
    }

    @Test
    @Transactional
    public void getCoursesTrue() {
        var list = teachingRegisterService.getCourses("MGV002");
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getSubjectCode(), "SQA");
        assertEquals(list.get(1).getSubjectCode(), "OOP");
    }

    @Test
    public void getCoursesTeacherCodeNotFound() {
        var list = teachingRegisterService.getCourses("MGV003");
        assertNull(list);
    }

    @Test
    public void getCoursesTeacherCodeNull() {
        var list = teachingRegisterService.getCourses(null);
        assertNull(list);
    }

    @AfterAll
    public void tearDownAfterClass() throws Exception {
        courseRepository.delete(course);
        courseRepository.delete(course2);
        teacherRepository.delete(teacher);
    }
}