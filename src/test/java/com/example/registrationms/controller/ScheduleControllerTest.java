package com.example.registrationms.controller;

import com.example.registrationms.dto.ScheduleDTO;
import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.model.Schedule;
import com.example.registrationms.model.WeekDay;
import com.example.registrationms.respository.ScheduleRepository;
import com.example.registrationms.service.TeachingRegisterService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleControllerTest {
    @Autowired
    private TeachingRegisterService registerService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private Schedule schedule1;
    private Schedule schedule2;

    @BeforeEach
    public void setUp() {
        // Khởi tạo các Schedule
        schedule1 = Schedule.builder()
                .week(1)
                .weekDay(WeekDay.MONDAY)
                .shift(1)
                .build();

        schedule2 = Schedule.builder()
                .week(1)
                .weekDay(WeekDay.TUESDAY)
                .shift(2)
                .build();

        // Lưu các Schedule vào repository
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
    }
    @Test
    void testGetScheduleTrue() {
        var foundSchedule1 = registerService.getSchedule(schedule1.getId());
        var foundSchedule2 = registerService.getSchedule(schedule2.getId());

        // Assert
        assertNotNull(foundSchedule1);
        assertEquals(schedule1.getId(), foundSchedule1.getId());
        assertEquals(schedule1.getWeek(), foundSchedule1.getWeek());
        assertEquals(schedule1.getWeekDay().ordinal() + 2, foundSchedule1.getWeekDay()); //do Weekday là enum, bắt đầu là Monday(tương đương với ordinal là 0)
        assertEquals(schedule1.getShift(), foundSchedule1.getShift());

        assertNotNull(foundSchedule2);
        assertEquals(schedule2.getId(), foundSchedule2.getId());
        assertEquals(schedule2.getWeek(), foundSchedule2.getWeek());
        assertEquals(schedule2.getWeekDay().ordinal() + 2, foundSchedule2.getWeekDay());
        assertEquals(schedule2.getShift(), foundSchedule2.getShift());
    }

    //test get schedule không tồn tại ID
    @Test
    void testGetScheduleNonexistentId() {
        var foundSchedule = registerService.getSchedule(-1);

        assertNull(foundSchedule);
    }

    //ném ra ngoại lệ hoặc trả về null khi ID lịch học không tồn tại.
    @Test
    void testGetScheduleNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            registerService.getSchedule(null);
        });
    }
}