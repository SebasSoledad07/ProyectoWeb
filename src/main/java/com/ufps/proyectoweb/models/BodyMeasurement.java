package com.ufps.proyectoweb.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BodyMeasurement {
    private int id;
    private User client;
    private LocalDate date;
    private Double weight;
    private Double height;
    private Double waistMeasurement;
    private Double armMeasurement;
    private Double legMeasurement;
    private Double bmi;

}

