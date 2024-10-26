package com.dw.proyecto_final.controllers;

import com.dw.proyecto_final.dtos.UsuarioDTO;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;


@PostMapping("/api/usuarios")
public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
    Usuario nuevoUsuario = usuarioService.crearUsuarioConRol(usuarioDTO);
    return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
}

}
