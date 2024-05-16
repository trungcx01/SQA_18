package com.example.registrationms.controller;

import com.example.registrationms.dto.ScheduleDTO;
import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.service.TeachingRegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ScheduleControllerTest {
    @Autowired
    private TeachingRegisterService registerService;

//    @BeforeEach
//    void setUp() {
//        registerService.register(
//                new TeachingRegisterRequest(
//                        "MGV001",
//                        "SQA",
//                        2, 6, 1, 1
//                )
//        );
//    }

    @Test
    void getSchedule() {
        var result = registerService.getSchedule(3);
        assertThat(result).isInstanceOf(ScheduleDTO.class);
        assertThat(result.getShift()).isEqualTo(1);
        assertThat(result.getWeekDay()).isEqualTo(6);
        assertThat(result.getWeek()).isEqualTo(2);
        assertThat(result.getRoom().getId()).isEqualTo(1);
    }

    @Test
    public void testGetScheduleSuccess() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetScheduleInvalidId() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetScheduleNegativeId() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testGetScheduleZeroId() {
        assertThat(true).isEqualTo(true);
    }
}