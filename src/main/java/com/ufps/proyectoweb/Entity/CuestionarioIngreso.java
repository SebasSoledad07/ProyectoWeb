package com.ufps.proyectoweb.Entity;

import java.time.LocalDate;

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
