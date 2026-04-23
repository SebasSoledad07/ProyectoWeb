package com.ufps.proyectoweb.Entity;

import java.util.List;

public class Rutina {

    private int id;
    private String nombre;
    private String descripcion;
    private Usuario entrenador;
    private Usuario cliente;
    private List<DetalleRutina> ejerciciosAsignados;

}
