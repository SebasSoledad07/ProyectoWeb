package com.ufps.proyectoweb.services;

import com.ufps.proyectoweb.dto.user.HealthQuestionnaireRequest;
import com.ufps.proyectoweb.dto.user.LoginRequest;
import com.ufps.proyectoweb.dto.user.RegisterUserRequest;
import com.ufps.proyectoweb.dto.user.UpdateUserRequest;
import com.ufps.proyectoweb.dto.user.UserResponse;
import com.ufps.proyectoweb.entity.IntakeQuestionnaire;
import com.ufps.proyectoweb.entity.User;
import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import com.ufps.proyectoweb.enums.WeekDay;
import com.ufps.proyectoweb.mapper.UserMapper;
import com.ufps.proyectoweb.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the user service.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new UserMapper(), passwordEncoder);
    }

    @Test
    void registerShouldPersistNewUser() {
        RegisterUserRequest request = new RegisterUserRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT
        );

        when(userRepository.existsByEmailIgnoreCase("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        assertEquals("john@example.com", userCaptor.getValue().getEmail());
        assertEquals("hashed-password", userCaptor.getValue().getPassword());
        assertEquals("John", response.firstName());
    }

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT
        );

        when(userRepository.existsByEmailIgnoreCase("john@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticateShouldReturnUserWhenCredentialsAreValid() {
        User user = new User(
                1L,
                "John",
                "Doe",
                "john@example.com",
                "hashed-password",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT,
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);

        UserResponse response = userService.authenticate(new LoginRequest("john@example.com", "password123", Role.CLIENT));

        assertEquals(1L, response.id());
        assertEquals("john@example.com", response.email());
    }

    @Test
    void authenticateShouldFailWhenPasswordIsInvalid() {
        User user = new User(
                1L,
                "John",
                "Doe",
                "john@example.com",
                "hashed-password",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT,
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.authenticate(new LoginRequest("john@example.com", "wrong", Role.CLIENT)));
    }

    @Test
    void addHealthQuestionnaireShouldAttachQuestionnaireToUser() {
        User user = new User(
                1L,
                "John",
                "Doe",
                "john@example.com",
                "hashed-password",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthQuestionnaireRequest request = new HealthQuestionnaireRequest(
                LocalDate.of(2026, 5, 20),
                "None",
                "None",
                "High",
                "Improve strength",
                WeekDay.MONDAY
        );

        userService.addHealthQuestionnaire(1L, request);

        assertEquals(1, user.getMedicalQuestionnaires().size());
        IntakeQuestionnaire questionnaire = user.getMedicalQuestionnaires().get(0);
        assertEquals("Improve strength", questionnaire.getGoal());
        assertEquals(user, questionnaire.getClient());
    }

    @Test
    void updateShouldModifyAllowedFields() {
        User user = new User(
                1L,
                "John",
                "Doe",
                "john@example.com",
                "hashed-password",
                "3001234567",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                Role.CLIENT,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.update(1L, new UpdateUserRequest(
                "Jane",
                "Smith",
                "3110000000",
                Gender.FEMALE,
                LocalDate.of(1994, 2, 2),
                Role.TRAINER
        ));

        assertEquals("Jane", response.firstName());
        assertEquals(Role.TRAINER, response.role());
        assertEquals("Jane", user.getFirstName());
    }
}

