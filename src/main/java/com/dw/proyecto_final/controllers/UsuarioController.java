package com.dw.proyecto_final.controllers;

import com.dw.proyecto_final.dtos.UsuarioDTO;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/usuarios")
@CrossOrigin("http://localhost:4200")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

@PostMapping("/nuevo")
public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
    Usuario nuevoUsuario = usuarioService.crearUsuarioConRol(usuarioDTO);
    return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
}

}
