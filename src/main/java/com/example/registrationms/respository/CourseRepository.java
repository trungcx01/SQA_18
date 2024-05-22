package com.example.registrationms.respository;

import com.example.registrationms.model.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByTeacherCode(String teacherCode);
    @Modifying
    @Transactional
    void deleteByTeacherCode(String teacherCode);
}
