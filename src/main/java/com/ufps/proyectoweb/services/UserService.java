package com.ufps.proyectoweb.services;

import com.ufps.proyectoweb.enums.Role;
import com.ufps.proyectoweb.models.BodyMeasurement;
import com.ufps.proyectoweb.models.IntakeQuestionnaire;
import com.ufps.proyectoweb.models.Reservation;
import com.ufps.proyectoweb.models.Routine;
import com.ufps.proyectoweb.models.User;
import com.ufps.proyectoweb.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean emailExists(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return userRepository.existsByEmail(normalizeEmail(email));
    }

    public User register(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        String key = normalizeEmail(user.getEmail());
        if (userRepository.existsByEmail(key)) {
            throw new IllegalArgumentException("El correo ya existe");
        }

        user.setEmail(key);
        ensureCollections(user);
        return userRepository.save(user);
    }

    public User authenticate(String email, String password, Role role) {
        if (email == null || password == null || role == null) {
            return null;
        }

        User user = userRepository.findByEmail(normalizeEmail(email));
        if (user == null) {
            return null;
        }

        boolean validPassword = password.equals(user.getPassword());
        boolean validRole = role.equals(user.getRole());
        return (validPassword && validRole) ? user : null;
    }

    public void saveHealthForm(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        ensureCollections(user);
        user.setEmail(normalizeEmail(user.getEmail()));
        userRepository.save(user);
    }

    private void ensureCollections(User user) {
        if (user.getReservations() == null) {
            user.setReservations(new ArrayList<Reservation>());
        }
        if (user.getAssignedRoutines() == null) {
            user.setAssignedRoutines(new ArrayList<Routine>());
        }
        if (user.getMedicalQuestionnaires() == null) {
            user.setMedicalQuestionnaires(new ArrayList<IntakeQuestionnaire>());
        }
        if (user.getBodyMeasurementHistory() == null) {
            user.setBodyMeasurementHistory(new ArrayList<BodyMeasurement>());
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
