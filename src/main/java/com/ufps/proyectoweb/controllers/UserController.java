package com.ufps.proyectoweb.controllers;

import com.ufps.proyectoweb.dto.HealthQuestionnaireRequest;
import com.ufps.proyectoweb.dto.HealthQuestionnaireResponse;
import com.ufps.proyectoweb.dto.UpdateUserRequest;
import com.ufps.proyectoweb.dto.UserResponse;
import com.ufps.proyectoweb.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for user profile and health data management.
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    /**
     * Creates the controller.
     *
     * @param userService user service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists all users.
     *
     * @return all user responses
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Finds a user by id.
     *
     * @param id user identifier
     * @return user response
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Updates a user profile.
     *
     * @param id user identifier
     * @param request update data
     * @return updated user response
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable @Positive Long id,
                                               @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /**
     * Deletes a user.
     *
     * @param id user identifier
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if an email is already registered.
     *
     * @param email email to check
     * @return email existence status
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> emailExists(@RequestParam @Email @NotBlank String email) {
        return ResponseEntity.ok(userService.emailExists(email));
    }

    /**
     * Adds a health questionnaire to a user.
     *
     * @param id user identifier
     * @param request questionnaire data
     * @return created questionnaire response
     */
    @PostMapping("/{id}/questionnaires")
    public ResponseEntity<HealthQuestionnaireResponse> addQuestionnaire(@PathVariable @Positive Long id,
                                                                         @Valid @RequestBody HealthQuestionnaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addHealthQuestionnaire(id, request));
    }

    /**
     * Lists the health questionnaires of a user.
     *
     * @param id user identifier
     * @return questionnaire responses
     */
    @GetMapping("/{id}/questionnaires")
    public ResponseEntity<List<HealthQuestionnaireResponse>> findQuestionnaires(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.findHealthQuestionnaires(id));
    }
}
