package com.example.registrationms.controller;

import com.example.registrationms.dto.CourseDTO;
import com.example.registrationms.dto.TeachingRegisterRequest;
import com.example.registrationms.dto.ResponseObject;
import com.example.registrationms.exception.DuplicateRegisterException;
import com.example.registrationms.exception.ShiftInputException;
import com.example.registrationms.exception.WeekDayInputException;
import com.example.registrationms.exception.WeekInputException;
import com.example.registrationms.model.Course;
import com.example.registrationms.service.TeachingRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegisterController {
    private final TeachingRegisterService registerService;
    @GetMapping("/{teacherCode}")
    public ResponseEntity<List<CourseDTO>> getRegister(@PathVariable String teacherCode) {
        var courses = registerService.getCourses(teacherCode);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> register(@RequestBody TeachingRegisterRequest request) {
        var response = new ResponseObject();
        try {
            registerService.register(request);
        } catch (WeekInputException e) {
            response.setMessage("Số tuần không hợp lệ");
            return ResponseEntity.ok(response);
        } catch (WeekDayInputException e) {
            response.setMessage("Số ngày không hợp lệ");
            return ResponseEntity.ok(response);
        } catch (ShiftInputException e) {
            response.setMessage("Số kíp không hợp lệ");
            return ResponseEntity.ok(response);
        } catch (DuplicateRegisterException e) {
            response.setMessage("Kíp học bị trùng");
            return ResponseEntity.ok(response);
        }
        response.setMessage("Đăng ký thành công");
        return ResponseEntity.ok(response);
    }
}