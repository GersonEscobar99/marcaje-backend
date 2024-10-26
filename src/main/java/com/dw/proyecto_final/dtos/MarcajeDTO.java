package com.dw.proyecto_final.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcajeDTO {
    private Long idMarcaje;
    private Long idUsuario;
    private String username;
    private LocalDate fechaMarcaje;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Boolean dentroDeHorario;
}
