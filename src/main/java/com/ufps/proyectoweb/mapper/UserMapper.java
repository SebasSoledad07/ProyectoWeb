package com.ufps.proyectoweb.mapper;

import com.ufps.proyectoweb.dto.HealthQuestionnaireRequest;
import com.ufps.proyectoweb.dto.HealthQuestionnaireResponse;
import com.ufps.proyectoweb.dto.RegisterUserRequest;
import com.ufps.proyectoweb.dto.UpdateUserRequest;
import com.ufps.proyectoweb.dto.UserResponse;
import com.ufps.proyectoweb.entity.IntakeQuestionnaire;
import com.ufps.proyectoweb.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralizes user entity and DTO mappings.
 */
@Component
public class UserMapper {

    /**
     * Maps a registration request to a new user entity.
     *
     * @param request registration request
     * @return user entity
     */
    public User toEntity(RegisterUserRequest request) {
        return new User(
                null,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                request.phoneNumber(),
                request.gender(),
                request.birthDate(),
                request.role(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    /**
     * Applies update data to an existing user entity.
     *
     * @param user existing user
     * @param request update request
     */
    public void applyUpdate(User user, UpdateUserRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setGender(request.gender());
        user.setBirthDate(request.birthDate());
        user.setRole(request.role());
    }

    /**
     * Maps a user entity to a response DTO.
     *
     * @param user user entity
     * @return response DTO
     */
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getBirthDate(),
                user.getRole(),
                sizeOf(user.getMedicalQuestionnaires()),
                sizeOf(user.getBodyMeasurementHistory())
        );
    }

    /**
     * Maps a questionnaire request to an entity attached to a user.
     *
     * @param request questionnaire request
     * @param user owning user
     * @return questionnaire entity
     */
    public IntakeQuestionnaire toQuestionnaireEntity(HealthQuestionnaireRequest request, User user) {
        return new IntakeQuestionnaire(
                null,
                user,
                request.intakeDate(),
                request.previousInjuries(),
                request.illnesses(),
                request.physicalActivityLevel(),
                request.goal(),
                request.availableDays()
        );
    }

    /**
     * Maps a questionnaire entity to a response DTO.
     *
     * @param questionnaire questionnaire entity
     * @return response DTO
     */
    public HealthQuestionnaireResponse toQuestionnaireResponse(IntakeQuestionnaire questionnaire) {
        return new HealthQuestionnaireResponse(
                questionnaire.getId(),
                questionnaire.getIntakeDate(),
                questionnaire.getPreviousInjuries(),
                questionnaire.getIllnesses(),
                questionnaire.getPhysicalActivityLevel(),
                questionnaire.getGoal(),
                questionnaire.getAvailableDays()
        );
    }

    private int sizeOf(List<?> values) {
        return values == null ? 0 : values.size();
    }
}

