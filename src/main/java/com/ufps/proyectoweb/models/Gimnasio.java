package com.ufps.proyectoweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Gimnasio {
    private int id;
    private String nombre;
    private String direccion;
    private Reserva reserva;
}
