package com.example.registrationms.controller;
import com.example.registrationms.dto.SubjectDTO;
import com.example.registrationms.model.Course;
import com.example.registrationms.model.Subject;
import com.example.registrationms.model.Teacher;
import com.example.registrationms.respository.CourseRepository;
import com.example.registrationms.respository.SubjectRepository;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubjectControllerTest {
    @Autowired
    private TeachingRegisterService registerService;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private CourseRepository courseRepository;

    private Subject subject1;
    private Subject subject2;
    private Teacher teacher;
    private Course course1;
    private Course course2;
    @BeforeAll
    public void setUpBeforeClass() throws Exception {
        // Khởi tạo các Subject
        subject1 = new Subject("SQA", "Software Quality Assurance");
        subject2 = new Subject("OOP", "Object-Oriented Programming");
        // Lưu các Subject vào repository
        subjectRepo.save(subject1);
        subjectRepo.save(subject2);
        // Khởi tạo các Course liên quan đến mỗi Subject
        course1 = Course.builder()
                .subject(subject1)
                .name("SQA Course 1")
                .build();
        course2 = Course.builder()
                .subject(subject1)
                .name("SQA Course 2")
                .build();
        // Lưu các Course vào repository
        courseRepository.save(course1);
        courseRepository.save(course2);
    }
    @Test
    void testGetSubject() {
        var foundSubject1 = registerService.getSubject("SQA");
        var foundSubject2 = registerService.getSubject("OOP");
        assertNotNull(foundSubject1);
        assertEquals("SQA", foundSubject1.getCode());
        assertEquals("Software Quality Assurance", foundSubject1.getName());
        assertNotNull(foundSubject2);
        assertEquals("OOP", foundSubject2.getCode());
        assertEquals("Object-Oriented Programming", foundSubject2.getName());
    }
    @Test
    void testGetSubjectNonexistentCode() {
        var foundSubject = registerService.getSubject("NonexistentCode");

        // Assert
        assertNull(foundSubject);
    }
    @Test
    void testGetSubjectWithNullOrEmptyCode() {
        var foundSubjectWithNullCode = registerService.getSubject(null);
        var foundSubjectWithEmptyCode = registerService.getSubject("");

        assertNull(foundSubjectWithNullCode);
        assertNull(foundSubjectWithEmptyCode);
    }
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
}