package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.Gender;
import com.ufps.proyectoweb.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Gender gender;
    private LocalDate birthDate;
    private Role role;
    private List<Reservation> reservations;
    private List<Routine> assignedRoutines;
    private List<IntakeQuestionnaire> medicalQuestionnaires;
    private List<BodyMeasurement> bodyMeasurementHistory;


}

