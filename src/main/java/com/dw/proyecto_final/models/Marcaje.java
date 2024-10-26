package com.dw.proyecto_final.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marcaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMarcaje;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_marcaje", nullable = false)
    private LocalDate fechaMarcaje;

    @Column(name = "hora_entrada", nullable = true)
    private LocalTime horaEntrada;

    @Column(name = "hora_salida", nullable = true)
    private LocalTime horaSalida;

    @Column(name = "dentro_de_horario", nullable = false)
    private Boolean dentroDeHorario;
}
