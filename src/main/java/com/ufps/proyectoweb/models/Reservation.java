package com.ufps.proyectoweb.models;

import com.ufps.proyectoweb.enums.ReservationStatus;
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
public class Reservation {
    private int id;
    private User client;
    private User trainer;
    private Gym gym;
    private ReservationStatus reservationStatus;
    private Date date;
    private LocalDate startTime;
    private LocalDate endTime;
}

