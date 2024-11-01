package com.dw.proyecto_final.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Long rolId;
    private Long idHorario;
    private Long idDepartamento;
    private String nombreDepartamento;
}
