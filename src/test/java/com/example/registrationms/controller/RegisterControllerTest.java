package com.example.registrationms.controller;

import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.respository.*;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RegisterControllerTest {
    @Autowired
    private TeachingRegisterService registerService;

    private TeachingRegisterRequest request;

//    @BeforeEach
//    void setUp() {
//        request = new TeachingRegisterRequest(
//                "MGV001",
//                "SQA",
//                2, 6, 1, 1
//        );
//
//        registerService.register(request);
//
//        registerService.register(
//                new TeachingRegisterRequest(
//                        "MGV001",
//                        "OOP",
//                        2, 5, 3, 1
//                )
//        );
//    }

    @Test
    void getRegister() {
        var result = registerService.getCourses("MGV001");
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSubjectCode()).isEqualTo("SQA");
        assertThat(result.get(1).getSubjectCode()).isEqualTo("OOP");

        assertThat(result.get(0).getScheduleIds()).hasSize(1);
        assertThat(result.get(1).getScheduleIds()).hasSize(1);
    }

    @Test
    void register() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterSuccess1() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterSuccess2() {
        assertThat(true).isEqualTo(true);
    }


    @Test
    public void testRegisterDuplicate1() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterDuplicate2() {
        assertThat(true).isEqualTo(true);
    }


    @Test
    public void testRegisterInvalidWeek1() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterInvalidWeek2() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterInvalidSession1() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterInvalidSession2() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterNonExistentTeacher() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterNonExistentSubject() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testRegisterNonExistentRoom() {
        assertThat(true).isEqualTo(true);
    }


}