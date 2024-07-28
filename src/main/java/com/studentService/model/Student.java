package com.studentService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Student {
    @Id
    private String rollNumber;
    private String studentName;
    private int science;
    private int maths;
    private int english;
    private int computer;
    private String eligible;

}
