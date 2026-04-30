package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class IntakeQuestionnaire {

    private int id;
    private User client;
    private LocalDate intakeDate;
    private String previousInjuries;
    private String illnesses;
    private String physicalActivityLevel;
    private String goal;
    private WeekDay availableDays;
}

