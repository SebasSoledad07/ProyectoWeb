package com.ufps.proyectoweb.Entity;

import java.time.LocalDate;
import java.util.Date;

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
