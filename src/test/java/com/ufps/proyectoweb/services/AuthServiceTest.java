package com.ufps.proyectoweb.services;

import com.ufps.proyectoweb.dto.user.LoginRequest;
import com.ufps.proyectoweb.dto.user.RegisterUserRequest;
import com.ufps.proyectoweb.dto.user.UserResponse;
import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the auth service.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService);
    }

    @Test
    void registerShouldDelegateToUserService() {
        UserResponse response = new UserResponse(1L, "John", "Doe", "john@example.com", "3001234567", Gender.MALE, LocalDate.of(1995, 1, 1), Role.CLIENT, 0, 0);
        when(userService.register(any(RegisterUserRequest.class))).thenReturn(response);

        UserResponse result = authService.register(new RegisterUserRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT
        ));

        assertEquals("john@example.com", result.email());
        verify(userService).register(any(RegisterUserRequest.class));
    }

    @Test
    void loginShouldDelegateToUserService() {
        UserResponse response = new UserResponse(1L, "John", "Doe", "john@example.com", "3001234567", Gender.MALE, LocalDate.of(1995, 1, 1), Role.CLIENT, 0, 0);
        when(userService.authenticate(any(LoginRequest.class))).thenReturn(response);

        UserResponse result = authService.login(new LoginRequest("john@example.com", "password123", Role.CLIENT));

        assertEquals("john@example.com", result.email());
        verify(userService).authenticate(any(LoginRequest.class));
    }
}

