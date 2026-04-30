package com.ufps.proyectoweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Gym {
    private int id;
    private String name;
    private String address;
    private Reservation reservation;
}

