package com.ufps.proyectoweb.entity;

import com.ufps.proyectoweb.enums.WeekDay;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "intake_questionnaires")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntakeQuestionnaire {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(name = "intake_date", nullable = false)
    private LocalDate intakeDate;

    @Column(name = "previous_injuries", columnDefinition = "TEXT")
    private String previousInjuries;

    @Column(columnDefinition = "TEXT")
    private String illnesses;

    @Column(name = "physical_activity_level", nullable = false, length = 100)
    private String physicalActivityLevel;

    @Column(nullable = false, length = 255)
    private String goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "available_day", nullable = false, length = 20)
    private WeekDay availableDays;
}
