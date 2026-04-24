package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CuestionarioIngreso {

    private int id;
    private Usuario cliente;
    private LocalDate fechaIngreso;
    private String lesionesPrevias;
    private String enfermedades;
    private String nivelActividadFisica;
    private String objetivo;
    private DiaSemana diasDisponibles;
}
