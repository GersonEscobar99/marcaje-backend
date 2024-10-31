package com.dw.proyecto_final.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.dw.proyecto_final.controllers.request.CrearUsuarioDTO;
import com.dw.proyecto_final.models.Rol;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.repositories.RolRepository;
import com.dw.proyecto_final.repositories.UsuarioRepository;
import com.dw.proyecto_final.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;



@RestController

public class MainController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;


    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "Hola no Seguro";
    }

    @GetMapping("/helloSecured")
    public String helloSecure() {
        return "Hola Seguro";
    }



    @PostMapping("/createUser")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO) {
        // Busca los roles en la base de datos a partir de los nombres que vienen en el DTO
        List<Rol> roles = crearUsuarioDTO.getRoles().stream()
                .map(roleName -> rolRepository.findByNombreRol(roleName)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
                .collect(Collectors.toList());

                String hashedPassword = passwordEncoder.encode(crearUsuarioDTO.getPassword());

        // Crea el usuario
        Usuario usuario = Usuario.builder()
                .username(crearUsuarioDTO.getUsername())
                .password(hashedPassword)
                .nombre(crearUsuarioDTO.getNombre())
                .apellido(crearUsuarioDTO.getApellido())
                .email(crearUsuarioDTO.getEmail())
                .telefono(crearUsuarioDTO.getTelefono())
                .enabled(true)
                .roles(new HashSet<>(roles)) // Asegúrate de que roles sea un Set
                .build();

                usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
    }


}
