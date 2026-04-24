package com.ufps.proyectoweb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DetalleRutina {

    private int id;
    private Ejercicio ejercicio;
    private String repeticiones;
    private Double pesoSugerido;
    private String series;
}
