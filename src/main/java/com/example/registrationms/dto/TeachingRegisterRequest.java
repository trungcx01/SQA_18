package com.example.registrationms.dto;

public record TeachingRegisterRequest(
        String teacherCode,
        String subjectCode,
        int week,
        int weekDay,
        int shift,
        int roomId
) {
}
