package com.ufps.proyectoweb.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MedidaCorporal {
    private int id;
    private Usuario cliente;
    private LocalDate fecha;
    private Double peso;
    private Double altura;
    private Double medidaCintura;
    private Double medidaBrazo;
    private Double medidaPierna;
    private Double imc;

}
