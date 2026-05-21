package com.ufps.proyectoweb.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "body_measurements")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BodyMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(name = "measurement_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(name = "waist_measurement")
    private Double waistMeasurement;

    @Column(name = "arm_measurement")
    private Double armMeasurement;

    @Column(name = "leg_measurement")
    private Double legMeasurement;

    @Column(nullable = false)
    private Double bmi;

}
