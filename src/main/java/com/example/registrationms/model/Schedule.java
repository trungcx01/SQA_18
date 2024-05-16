package com.example.registrationms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Schedule {
    @Id @GeneratedValue
    private Integer id;
    private WeekDay weekDay;
    private int shift;
    private int week;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    private Classroom room;

    public Schedule(int week, int weekDay, int shift) {
        this.week = week;
        this.weekDay = switch (weekDay) {
            case 2 -> WeekDay.MONDAY;
            case 3 -> WeekDay.TUESDAY;
            case 4 -> WeekDay.WEDNESDAY;
            case 5 -> WeekDay.THURSDAY;
            case 6 -> WeekDay.FRIDAY;
            case 7 -> WeekDay.SATURDAY;
            default -> WeekDay.UNDEFINED;
        };
        this.shift = shift;
    }
}
