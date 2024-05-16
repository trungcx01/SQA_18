package com.example.registrationms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private String subjectCode;
    private String teacherCode;
    private List<Integer> scheduleIds;
}
