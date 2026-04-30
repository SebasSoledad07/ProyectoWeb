package com.ufps.proyectoweb.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Routine {

    private int id;
    private String name;
    private String description;
    private User trainer;
    private User client;
    private List<RoutineDetail> assignedExercises;

}

