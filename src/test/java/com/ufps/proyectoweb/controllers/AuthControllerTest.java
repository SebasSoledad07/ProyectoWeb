package com.ufps.proyectoweb.controllers;

import com.ufps.proyectoweb.dto.user.LoginRequest;
import com.ufps.proyectoweb.dto.user.RegisterUserRequest;
import com.ufps.proyectoweb.dto.user.UserResponse;
import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import com.ufps.proyectoweb.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the auth controller.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void registerShouldReturnCreatedStatus() {
        UserResponse response = new UserResponse(1L, "John", "Doe", "john@example.com", "3001234567", Gender.MALE, LocalDate.of(1995, 1, 1), Role.CLIENT, 0, 0);
        when(authService.register(any(RegisterUserRequest.class))).thenReturn(response);

        ResponseEntity<UserResponse> result = authController.register(new RegisterUserRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT
        ));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john@example.com", result.getBody().email());
        verify(authService).register(any(RegisterUserRequest.class));
    }

    @Test
    void loginShouldReturnOkStatus() {
        UserResponse response = new UserResponse(1L, "John", "Doe", "john@example.com", "3001234567", Gender.MALE, LocalDate.of(1995, 1, 1), Role.CLIENT, 0, 0);
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        ResponseEntity<UserResponse> result = authController.login(new LoginRequest("john@example.com", "password123", Role.CLIENT));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("john@example.com", result.getBody().email());
        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void logoutShouldReturnNoContent() {
        ResponseEntity<Void> result = authController.logout();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(authService).logout();
    }
}
