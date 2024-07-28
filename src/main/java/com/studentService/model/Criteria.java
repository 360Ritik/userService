package com.studentService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class Criteria {

    private int scienceCriteria=85;
    private int mathsCriteria=90;
    private int englishCriteria=75;
    private int computerCriteria=95;

    public Criteria(int scienceCriteria, int mathsCriteria, int englishCriteria, int computerCriteria) {
        this.scienceCriteria = scienceCriteria;
        this.mathsCriteria = mathsCriteria;
        this.englishCriteria = englishCriteria;
        this.computerCriteria = computerCriteria;
    }
}
