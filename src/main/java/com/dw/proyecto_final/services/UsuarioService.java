package com.dw.proyecto_final.services;

import com.dw.proyecto_final.dtos.UsuarioDTO;
import com.dw.proyecto_final.models.*;
import com.dw.proyecto_final.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    HorarioRepository horarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    MarcajeRepository marcajeRepository;

    public Usuario crearUsuarioConRol(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());

        Rol rol = rolRepository.findById(usuarioDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + usuarioDTO.getRolId()));

        Horario horario = horarioRepository.findById(usuarioDTO.getIdHorario())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + usuarioDTO.getIdHorario()));


        Departamento departamento = departamentoRepository.findById(usuarioDTO.getIdDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con ID: " + usuarioDTO.getIdDepartamento()));

        usuario.getRoles().add(rol);
        usuario.setHorario(horario);
        usuario.setDepartamento(departamento);

        return usuarioRepository.save(usuario);
    }


    public Optional<Usuario> obtenerUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }
    


    public Optional<Usuario> obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username);
    }


    


}
