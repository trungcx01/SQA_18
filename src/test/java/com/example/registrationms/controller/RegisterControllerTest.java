package com.example.registrationms.controller;

import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.exception.*;
import com.example.registrationms.model.*;
import com.example.registrationms.respository.*;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.registrationms.model.WeekDay.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegisterControllerTest {
    @Autowired
    private TeachingRegisterService registerService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private TeachingRegisterRequest request;

    @Autowired
    private PasswordEncoder encoder;
    private Classroom room;
    private Teacher teacher1, teacher2;
    private Subject subject1, subject2;

    @BeforeEach
    void setUp() {
        room = Classroom.builder()
                .name("703-A3")
                .build();

        teacher1 = Teacher.builder()
                .dob(LocalDate.of(1999, 11, 10))
                .address("Ha Giang")
                .build();
        teacher1.setCode("MGV005");
        teacher1.setName("Cao Van C");
        teacher1.setUsername("MGV005");
        teacher1.setPassword(encoder.encode("1234567"));
        teacher1.setRole(Role.TEACHER);
        teacher1.setCourses(new ArrayList<>());

        teacher2 = Teacher.builder()
                .dob(LocalDate.of(1990, 11, 1))
                .address("Nghệ An")
                .build();
        teacher2.setCode("MGV006");
        teacher2.setName("Cao Xuân Trung");
        teacher2.setUsername("MGV006");
        teacher2.setPassword(encoder.encode("12345678"));
        teacher2.setRole(Role.TEACHER);
        teacher2.setCourses(new ArrayList<>());


        subject1 = Subject.builder()
                .code("SQT")
                .name("Đảm bảo chất lượng phần mềm HN")
                .build();

        subject2 = Subject.builder()
                .code("SOA")
                .name("Phát triển phần mềm HDV")
                .build();

        // Lưu giáo viên, môn học và lớp học vào cơ sở dữ liệu
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        classroomRepository.save(room);
    }

    @Test
    void testRegisterTeachingSuccess() {
        // GV Đăng kí lịch thành công
        TeachingRegisterRequest request = new TeachingRegisterRequest(teacher1.getCode(), subject1.getCode(), 2, 2, 2, room.getId());

        // Thực thi phương thức register
        boolean result = registerService.register(request);

        // Kiểm tra xem phương thức register trả về true
        assertTrue(result);

        // Lấy giáo viên đã lưu từ cơ sở dữ liệu
        Optional<Teacher> savedTeacher = teacherRepository.findByTeacherCode("MGV005");
        assertTrue(savedTeacher.isPresent());
        assertEquals(1, savedTeacher.get().getCourses().size());

        // Kiểm tra thông tin của lớp học phần
        Course course = savedTeacher.get().getCourses().get(0);
        assertEquals("Đảm bảo chất lượng phần mềm HN", course.getSubject().getName());

        // Kiểm tra thông tin của lịch học
        Schedule schedule = course.getSchedules().get(0);
        assertEquals(2, schedule.getWeek());
        assertEquals(MONDAY, schedule.getWeekDay());
        assertEquals(2, schedule.getShift());
    }

    @Test
    void testRegisterConcurrentScheduleConflict1() {
        //2 GV đăng kí trùng lịch
        TeachingRegisterRequest requestForTeacher1 = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 2, 2, room.getId());
        boolean resultForTeacher1 = registerService.register(requestForTeacher1);
        assertTrue(resultForTeacher1);


        TeachingRegisterRequest requestForTeacher2 = new TeachingRegisterRequest(
                teacher2.getCode(), subject1.getCode(), 2, 2, 2, room.getId());

        assertThrows(DuplicateRegisterException.class, () -> {
            registerService.register(requestForTeacher2);
        });

        Optional<Teacher> savedTeacher1 = teacherRepository.findByTeacherCode(teacher1.getCode());
        assertTrue(savedTeacher1.isPresent(), "First teacher should be present in the database");
        assertEquals(1, savedTeacher1.get().getCourses().size(), "First teacher should have one course registered");

        Optional<Teacher> savedTeacher2 = teacherRepository.findByTeacherCode(teacher2.getCode());
        assertTrue(savedTeacher2.isPresent(), "Second teacher should be present in the database");
        assertEquals(0, savedTeacher2.get().getCourses().size(), "Second teacher should have no courses registered due to conflict");
    }

    @Test
    void testRegisterConcurrentScheduleConflict2() {
        //GV đăng kí 2 môn trùng 1 lịch
        TeachingRegisterRequest request1 = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 2, 2, room.getId());
        boolean resultForTeacher1 = registerService.register(request1);
        assertTrue(resultForTeacher1);


        TeachingRegisterRequest request2 = new TeachingRegisterRequest(
                teacher1.getCode(), subject2.getCode(), 2, 2, 2, room.getId());

        assertThrows(DuplicateRegisterException.class, () -> {
            registerService.register(request2);
        });

        Optional<Teacher> savedTeacher1 = teacherRepository.findByTeacherCode(teacher1.getCode());
        assertTrue(savedTeacher1.isPresent());
        assertEquals(1, savedTeacher1.get().getCourses().size());

    }

    @Test
    void testRegisterWithWeekOutsideAllowedRange1() {
        //đầu vào week = 4, hợp lệ
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 4, 3, 3, 1);
        assertTrue(registerService.register(request));
    }
    @Test
    void testRegisterWithWeekOutsideAllowedRange2() {
        //đầu vào week = 1, trường hợp biên
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 1, 3, 3, 1);
        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithWeekOutsideAllowedRange3() {
        //đầu vào week = 15, trường hợp biên
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 15, 3, 3, 1);
        assertTrue(registerService.register(request));
    }
    @Test
    void testRegisterWithWeekOutsideAllowedRange4() {
        // đầu vào week =0,  nhỏ hơn so với hợp lệ
        TeachingRegisterRequest requestWithWeekTooLow = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 0, 3, 2, 1); // Week 0, which is below the valid range
        assertThrows(WeekInputException.class, () -> {
            registerService.register(requestWithWeekTooLow);
        }, "Registering with week 0 should throw WeekInputException");

    }

    @Test
    void testRegisterWithWeekOutsideAllowedRange5() {
        // đầu vào week =20,  lớn hơn so với hợp lệ
        TeachingRegisterRequest requestWithWeekTooHigh = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 20, 3, 2, 1);
        assertThrows(WeekInputException.class, () -> {
            registerService.register(requestWithWeekTooHigh);
        }, "Registering with week 20 should throw WeekInputException");
    }


    @Test
    void testRegisterWithInvalidShift1() {
        //đầu vào kip học (shift) = 3, hợp lệ
        TeachingRegisterRequest requestWithShift = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 3, 1);
        assertTrue(registerService.register(requestWithShift));

    }
    @Test
    void testRegisterWithInvalidShift2() {
        //đầu vào kip học (shift) = 1, trường hợp biên
        TeachingRegisterRequest requestWithShift = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 1, 1);
        assertTrue(registerService.register(requestWithShift));

    }

    @Test
    void testRegisterWithInvalidShift3() {
        //đầu vào kip học (shift) = 6, trường hợp biên
        TeachingRegisterRequest requestWithShift = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 6, 1);
        assertTrue(registerService.register(requestWithShift));

    }
    @Test
    void testRegisterWithInvalidShift4() {
        //đầu vào kip học (shift) = 0,  nhỏ hơn so với hợp lệ
        TeachingRegisterRequest requestWithShiftTooLow = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 0, 1);
        assertThrows(ShiftInputException.class, () -> {
            registerService.register(requestWithShiftTooLow);
        }, "Registering with shift 0 should throw ShiftInputException");

    }

    @Test
    void testRegisterWithInvalidShift5() {
        //đầu vào kip học (shift) = 7,  lớn hơn so với hợp lệ
        TeachingRegisterRequest requestWithShiftTooHigh = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 3, 7, 1);
        assertThrows(ShiftInputException.class, () -> {
            registerService.register(requestWithShiftTooHigh);
        }, "Registering with shift 7 should throw ShiftInputException");
    }

    @Test
    void testRegisterWithInvalidWeekDay1() {
        //Trường hợp đầu vào của week day = 4,  hợp lệ
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 4, 3, 1);
        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithInvalidWeekDay2() {
        //Trường hợp đầu vào của week day = 2,  trường hợp biên
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 2, 3, 1);
        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithInvalidWeekDay3() {
        //Trường hợp đầu vào của week day = 7,  trường hợp biên
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 7, 3, 1);
        assertTrue(registerService.register(request));
    }


    @Test
    void testRegisterWithInvalidWeekDay4() {
        //Trường hợp đầu vào của week day = 1 (bé hơn weekday hơp lệ)
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 1, 2, 1);
        assertThrows(WeekDayInputException.class, ()->{
            registerService.register(request);
        });
    }

    @Test
    void testRegisterWithInvalidWeekDay5() {
        //Trường hợp đầu vào của week day = 8 (lớn hơn weekday hơp lệ)
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(),2, 8, 2, 1);
        assertThrows(WeekDayInputException.class, ()->{
            registerService.register(request);
        });
    }

    @Test
    void testRegisterWithValidClassroom() {
        // Scenario: Classroom ID is valid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 4, room.getId());

        // Assuming your register service is set up to handle this correctly
        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithInvalidClassroom() {
        // Scenario: Classroom ID is invalid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 2, 0);

        // Test to ensure the exception is thrown
        assertThrows(ClassroomNotFoundException.class, () -> {
            registerService.register(request);
        });
    }


    @Test
    void testRegisterWithValidSubject() {
        // Scenario: Subject ID is valid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 2, 3, 4, room.getId());

        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithInvalidSubject() {
        // Scenario: Subject ID is invalid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), "000", 2, 3, 3, room.getId());

        // Test to ensure the exception is thrown
        assertThrows(SubjectNotFoundException.class, () -> {
            registerService.register(request);
        });
    }

    @Test
    void testRegisterWithValidTeacher() {
        // Scenario: Teacher ID is valid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                teacher1.getCode(), subject1.getCode(), 1, 3, 4, room.getId());

        assertTrue(registerService.register(request));
    }

    @Test
    void testRegisterWithInvalidTeacher() {
        // Scenario: Teacher ID is invalid
        TeachingRegisterRequest request = new TeachingRegisterRequest(
                "MGV000", subject1.getCode(), 2, 3, 3, room.getId());

        // Test to ensure the exception is thrown
        assertThrows(TeacherNotFoundException.class, () -> {
            registerService.register(request);
        });
    }


//    @Test
//    void getRegister() {
//        var result = registerService.getCourses("MGV001");
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getSubjectCode()).isEqualTo("SQA");
//        assertThat(result.get(1).getSubjectCode()).isEqualTo("OOP");
//
//        assertThat(result.get(0).getScheduleIds()).hasSize(1);
//        assertThat(result.get(1).getScheduleIds()).hasSize(1);
//    }

}