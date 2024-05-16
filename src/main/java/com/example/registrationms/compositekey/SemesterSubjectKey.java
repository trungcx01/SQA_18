package com.example.registrationms.compositekey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SemesterSubjectKey implements Serializable {
    @Column(name = "semester_id")
    private Integer semesterId;
    @Column(name = "subject_id")
    private Integer subjectId;
}
