package com.ufps.proyectoweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RoutineDetail {

    private int id;
    private Exercise exercise;
    private String repetitions;
    private Double suggestedWeight;
    private String sets;
}

