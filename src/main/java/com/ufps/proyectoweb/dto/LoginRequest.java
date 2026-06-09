package com.ufps.proyectoweb.dto;

import com.ufps.proyectoweb.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload used to authenticate a user.
 *
 * @param email user's email
 * @param password user's password
 * @param role user's role
 */
public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull Role role
) {}

