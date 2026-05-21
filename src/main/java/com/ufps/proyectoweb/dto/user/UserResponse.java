package com.ufps.proyectoweb.dto.user;

import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;

import java.time.LocalDate;

/**
 * Response payload that exposes user profile information.
 *
 * @param id user identifier
 * @param firstName first name
 * @param lastName last name
 * @param email email
 * @param phoneNumber phone number
 * @param gender gender
 * @param birthDate birth date
 * @param role role
 * @param healthQuestionnaireCount questionnaire count
 * @param bodyMeasurementCount body measurement count
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate birthDate,
        Role role,
        int healthQuestionnaireCount,
        int bodyMeasurementCount
) {}

