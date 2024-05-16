package com.example.registrationms.dto;

import com.example.registrationms.model.Classroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleDTO {
    private int weekDay;
    private int shift;
    private int week;
    private Classroom room;
}
