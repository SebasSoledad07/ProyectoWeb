package com.ufps.proyectoweb.Entity;

import java.util.Date;
import java.util.List;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private String telefono;
    private Genero genero;
    private Date fechaNacimiento;
    private Rol rol;
    private List<Reserva> reservas;
    private List<Rutina> rutinasAsignadas;


}
