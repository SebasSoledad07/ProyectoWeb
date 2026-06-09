package com.ufps.proyectoweb.dto;

import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request payload used to register a new user.
 *
 * @param firstName user's first name
 * @param lastName user's last name
 * @param email user's email
 * @param password user's password
 * @param phoneNumber user's phone number
 * @param gender user's gender
 * @param birthDate user's birth date
 * @param role user's role
 */
public record RegisterUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String phoneNumber,
        @NotNull Gender gender,
        @NotNull LocalDate birthDate,
        @NotNull Role role
) {}

