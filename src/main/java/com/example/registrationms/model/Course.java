package com.example.registrationms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Course {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private int capacity = 60;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonIgnore
    private List<Schedule> schedules = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public void addSchedule(Schedule schedule) {
        schedule.setCourse(this);
        getSchedules().add(schedule);
    }

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            schedules = new ArrayList<>();
        }
        return schedules;
    }
}
