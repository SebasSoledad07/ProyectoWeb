package com.ufps.proyectoweb.dto.user;

import com.ufps.proyectoweb.enums.WeekDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request payload used to store a user's intake questionnaire.
 *
 * @param intakeDate questionnaire date
 * @param previousInjuries previous injuries description
 * @param illnesses current illnesses description
 * @param physicalActivityLevel physical activity level
 * @param goal user goal
 * @param availableDays available day
 */
public record HealthQuestionnaireRequest(
        @NotNull LocalDate intakeDate,
        @NotBlank String previousInjuries,
        @NotBlank String illnesses,
        @NotBlank String physicalActivityLevel,
        @NotBlank String goal,
        @NotNull WeekDay availableDays
) {}

