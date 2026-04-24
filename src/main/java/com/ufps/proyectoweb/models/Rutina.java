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
public class Rutina {

    private int id;
    private String nombre;
    private String descripcion;
    private Usuario entrenador;
    private Usuario cliente;
    private List<DetalleRutina> ejerciciosAsignados;

}
