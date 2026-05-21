package com.ufps.proyectoweb.services;

import com.ufps.proyectoweb.dto.user.HealthQuestionnaireRequest;
import com.ufps.proyectoweb.dto.user.HealthQuestionnaireResponse;
import com.ufps.proyectoweb.dto.user.LoginRequest;
import com.ufps.proyectoweb.dto.user.RegisterUserRequest;
import com.ufps.proyectoweb.dto.user.UpdateUserRequest;
import com.ufps.proyectoweb.dto.user.UserResponse;
import com.ufps.proyectoweb.entity.IntakeQuestionnaire;
import com.ufps.proyectoweb.entity.User;
import com.ufps.proyectoweb.enums.Role;
import com.ufps.proyectoweb.mapper.UserMapper;
import com.ufps.proyectoweb.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contains the business rules for the user module.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks whether a user email already exists.
     *
     * @param email email to check
     * @return true when the email is already registered
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return userRepository.existsByEmailIgnoreCase(normalizeEmail(email));
    }

    /**
     * Registers a new user.
     *
     * @param request registration data
     * @return persisted user response
     */
    public UserResponse register(RegisterUserRequest request) {
        validateRole(request.role());

        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("El correo ya existe");
        }

        User user = userMapper.toEntity(request);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Authenticates an existing user.
     *
     * @param request login credentials
     * @return authenticated user response
     */
    @Transactional(readOnly = true)
    public UserResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(normalizeEmail(request.email()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas o rol incorrecto."));

        if (!passwordEncoder.matches(request.password(), user.getPassword()) || user.getRole() != request.role()) {
            throw new IllegalArgumentException("Credenciales inválidas o rol incorrecto.");
        }

        return userMapper.toResponse(user);
    }

    /**
     * Returns all users in the system.
     *
     * @return user responses
     */
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Finds a user by identifier.
     *
     * @param id user identifier
     * @return user response
     */
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(getUserOrThrow(id));
    }

    /**
     * Updates an existing user.
     *
     * @param id user identifier
     * @param request update data
     * @return updated user response
     */
    public UserResponse update(Long id, UpdateUserRequest request) {
        validateRole(request.role());
        User user = getUserOrThrow(id);
        userMapper.applyUpdate(user, request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Deletes a user by identifier.
     *
     * @param id user identifier
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Adds a health questionnaire to a user.
     *
     * @param userId user identifier
     * @param request questionnaire data
     * @return questionnaire response
     */
    public HealthQuestionnaireResponse addHealthQuestionnaire(Long userId, HealthQuestionnaireRequest request) {
        User user = getUserOrThrow(userId);
        IntakeQuestionnaire questionnaire = userMapper.toQuestionnaireEntity(request, user);
        user.getMedicalQuestionnaires().add(questionnaire);
        userRepository.save(user);
        return userMapper.toQuestionnaireResponse(questionnaire);
    }

    /**
     * Returns all health questionnaires associated with a user.
     *
     * @param userId user identifier
     * @return questionnaire responses
     */
    @Transactional(readOnly = true)
    public List<HealthQuestionnaireResponse> findHealthQuestionnaires(Long userId) {
        User user = getUserOrThrow(userId);
        return user.getMedicalQuestionnaires().stream()
                .map(userMapper::toQuestionnaireResponse)
                .toList();
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
    }

    private void validateRole(Role role) {
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("ADMIN is not allowed in the user module.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
