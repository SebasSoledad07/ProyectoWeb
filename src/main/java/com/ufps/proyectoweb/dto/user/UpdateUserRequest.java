package com.ufps.proyectoweb.dto.user;

import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request payload used to update a user's profile.
 *
 * @param firstName user's first name
 * @param lastName user's last name
 * @param phoneNumber user's phone number
 * @param gender user's gender
 * @param birthDate user's birth date
 * @param role user's role
 */
public record UpdateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotNull Gender gender,
        @NotNull LocalDate birthDate,
        @NotNull Role role
) {}

