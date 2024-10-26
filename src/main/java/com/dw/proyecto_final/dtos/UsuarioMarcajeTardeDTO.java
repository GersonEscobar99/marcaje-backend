package com.dw.proyecto_final.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioMarcajeTardeDTO {
    private Long idUsuario;
    private String username;
    private String nombre;
    private LocalDateTime fechaMarcajeTarde;
}
