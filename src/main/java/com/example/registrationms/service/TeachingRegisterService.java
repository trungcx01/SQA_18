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
        var subject = subjectRepo.getReferenceById(subjectCode);
        return new SubjectDTO(subject.getCode(), subject.getName());
    }

    public ScheduleDTO getSchedule(int scheduleId) {
        var schedule = scheduleRepo.getReferenceById(scheduleId);
        var scheduleDTO = new ScheduleDTO();
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

        scheduleDTO.setWeekDay(weekDay);
        return scheduleDTO;
    }

    public boolean register(TeachingRegisterRequest request)  {
        boolean x = true;
            var teacher = teacherRepo.findByTeacherCode(request.teacherCode())
                    .orElseThrow(() -> new TeacherNotFoundException());
            var subject = subjectRepo.findById(request.subjectCode()).orElseThrow(() -> new SubjectNotFoundException());
            var room = classroomRepo.findById(request.roomId()).orElseThrow(() -> new ClassroomNotFoundException());

            int week = request.week(), weekDay = request.weekDay(), shift = request.shift();
            if (week < 1 || week > 15) {
                x = false;
                throw new WeekInputException();
            }
            if (weekDay < 2 || weekDay > 7) {
                x = false;
                throw new WeekDayInputException();
            }
            if (shift < 1 || shift > 6) {
                x = false;
                throw new ShiftInputException();
            }

            var wDay = switch (weekDay) {
                case 2 -> WeekDay.MONDAY;
                case 3 -> WeekDay.TUESDAY;
                case 4 -> WeekDay.WEDNESDAY;
                case 5 -> WeekDay.THURSDAY;
                case 6 -> WeekDay.FRIDAY;
                case 7 -> WeekDay.SATURDAY;
                default -> WeekDay.UNDEFINED;
            };

            var schedule = scheduleRepo.findByShiftAndWeekDayAndWeek(shift, wDay, week);
            if (schedule.isPresent()) {
                x = false;
                throw new DuplicateRegisterException();
            }

            var s = new Schedule(request.week(), request.weekDay(), request.shift());
            s.setRoom(room);

            var course = Course.builder()
                    .name("DEMO")
                    .subject(subject)
                    .build();

            course.addSchedule(s);

            teacher.addCourse(course);
            teacherRepo.save(teacher);
            return x;
    }
}
