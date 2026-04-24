package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.Genero;
import com.ufps.proyectoweb.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

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
    private List<CuestionarioIngreso> fichaMedica;
    private List<MedidaCorporal> historialMedidas;


}
