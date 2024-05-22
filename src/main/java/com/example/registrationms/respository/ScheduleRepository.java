package com.example.registrationms.respository;

import com.example.registrationms.model.Schedule;
import com.example.registrationms.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByShiftAndWeekDayAndWeek(int shift, WeekDay weekDay, int week);
    List<Schedule> findAllByCourseId(int courseId);

}
