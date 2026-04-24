package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Reserva {
    private int id;
    private Usuario cliente;
    private Usuario entrenador;
    private Gimnasio gimnasio;
    private EstadoReserva estadoReserva;
    private Date fecha;
    private LocalDate horaInicio;
    private LocalDate horaFin;
}
