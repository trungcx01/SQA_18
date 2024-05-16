package com.example.registrationms.respository;

import com.example.registrationms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends UserRepository {
    @Query("SELECT t from Teacher t where t.code = ?1")
    Optional<Teacher> findByTeacherCode(String teacherCode);
}
