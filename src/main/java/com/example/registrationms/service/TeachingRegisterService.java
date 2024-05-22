package com.example.registrationms.service;

import com.example.registrationms.dto.CourseDTO;
import com.example.registrationms.dto.ScheduleDTO;
import com.example.registrationms.dto.SubjectDTO;
import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.exception.*;
import com.example.registrationms.model.Course;
import com.example.registrationms.model.Schedule;
import com.example.registrationms.model.WeekDay;
import com.example.registrationms.respository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeachingRegisterService {
    private final ScheduleRepository scheduleRepo;
    private final SubjectRepository subjectRepo;
    private final TeacherRepository teacherRepo;
    private final ClassroomRepository classroomRepo;
    private final CourseRepository courseRepo;

    public List<CourseDTO> getAll() {
        return courseRepo.findAll()
                .stream()
                .map(course -> new CourseDTO(
                        course.getTeacher().getCode(),
                        course.getSubject().getCode(),
                        course.getSchedules().stream().map(schedule -> schedule.getId()).collect(Collectors.toList())))
                .toList();
    }

    public List<CourseDTO> getCourses(String teacherCode) {
        var teacher = teacherRepo.findByTeacherCode(teacherCode);
        if (teacher.isPresent()) {
            return teacher.get().getCourses()
                    .stream()
                    .map(course -> new CourseDTO(
                            course.getSubject().getCode(),
                            course.getTeacher().getCode(),
                            course.getSchedules().stream().map(schedule -> schedule.getId()).collect(Collectors.toList())
                    )).toList();
        }
        return null;
    }

    public SubjectDTO getSubject(String subjectCode) {
        var subject = subjectRepo.findById(subjectCode).orElse(null);
        if (subject==null) return null;
        return new SubjectDTO(subject.getCode(), subject.getName());
    }

    public ScheduleDTO getSchedule(Integer scheduleId) {
        var schedule = scheduleRepo.findById(scheduleId).orElse(null);
        if (schedule==null) return null;
        var scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setRoom(schedule.getRoom());
        scheduleDTO.setWeek(schedule.getWeek());
        scheduleDTO.setShift(schedule.getShift());

        var weekDay = switch (schedule.getWeekDay()) {
            case MONDAY -> 2;
            case TUESDAY -> 3;
            case WEDNESDAY -> 4;
            case THURSDAY -> 5;
            case FRIDAY -> 6;
            case SATURDAY -> 7;
            case UNDEFINED -> 0;
        };
//
        scheduleDTO.setWeekDay(weekDay);
        return scheduleDTO;
    }

    @Transactional
    public boolean register(TeachingRegisterRequest request) {
        var teacher = teacherRepo.findByTeacherCode(request.teacherCode())
                .orElseThrow(TeacherNotFoundException::new);
        var subject = subjectRepo.findById(request.subjectCode())
                .orElseThrow(SubjectNotFoundException::new);
        var room = classroomRepo.findById(request.roomId())
                .orElseThrow(ClassroomNotFoundException::new);

        int week = request.week(), weekDay = request.weekDay(), shift = request.shift();
        if (week < 1 || week > 15) {
            throw new WeekInputException();
        }
        if (weekDay < 2 || weekDay > 7) {
            throw new WeekDayInputException();
        }
        if (shift < 1 || shift > 6) {
            throw new ShiftInputException();
        }

        var weekDayEnum = switch (weekDay) {
            case 2 -> WeekDay.MONDAY;
            case 3 -> WeekDay.TUESDAY;
            case 4 -> WeekDay.WEDNESDAY;
            case 5 -> WeekDay.THURSDAY;
            case 6 -> WeekDay.FRIDAY;
            case 7 -> WeekDay.SATURDAY;
            default -> WeekDay.UNDEFINED;
        };

        var existingSchedule = scheduleRepo.findByShiftAndWeekDayAndWeek(shift, weekDayEnum, week);
        if (existingSchedule.isPresent()) {
            throw new DuplicateRegisterException();
        }

        var newSchedule = new Schedule(request.week(), request.weekDay(), request.shift());
        newSchedule.setRoom(room);

        var newCourse = Course.builder()
                .name("DEMO")
                .subject(subject)
                .build();
        newCourse.addSchedule(newSchedule);

        teacher.addCourse(newCourse);
        teacherRepo.save(teacher);

        return true;
    }
}
