package com.ufps.proyectoweb.dto.user;

import com.ufps.proyectoweb.enums.WeekDay;

import java.time.LocalDate;

/**
 * Response payload that exposes intake questionnaire information.
 *
 * @param id questionnaire identifier
 * @param intakeDate questionnaire date
 * @param previousInjuries previous injuries description
 * @param illnesses illnesses description
 * @param physicalActivityLevel physical activity level
 * @param goal user goal
 * @param availableDays available day
 */
public record HealthQuestionnaireResponse(
        Long id,
        LocalDate intakeDate,
        String previousInjuries,
        String illnesses,
        String physicalActivityLevel,
        String goal,
        WeekDay availableDays
) {}

