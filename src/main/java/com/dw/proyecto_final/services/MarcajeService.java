package com.dw.proyecto_final.services;

import com.dw.proyecto_final.dtos.MarcajeDTO;
import com.dw.proyecto_final.dtos.UsuarioDTO;
import com.dw.proyecto_final.models.Horario;
import com.dw.proyecto_final.models.Marcaje;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.repositories.MarcajeRepository;
import com.dw.proyecto_final.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarcajeService {

    @Autowired
    MarcajeRepository marcajeRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public MarcajeDTO registrarEntrada(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Marcaje marcaje = new Marcaje();
        marcaje.setUsuario(usuario);
        marcaje.setFechaMarcaje(LocalDate.now());
        marcaje.setHoraEntrada(LocalTime.now());

        Horario horarioUsuario = usuario.getHorario();
        marcaje.setDentroDeHorario(verificarHorarioEntrada(marcaje.getHoraEntrada(), horarioUsuario));

        Marcaje savedMarcaje = marcajeRepository.save(marcaje);

        return new MarcajeDTO(
            savedMarcaje.getIdMarcaje(),
            usuario.getIdUsuario(),
            usuario.getUsername(),
            savedMarcaje.getFechaMarcaje(),
            savedMarcaje.getHoraEntrada(),
            savedMarcaje.getHoraSalida(),
            savedMarcaje.getDentroDeHorario()
        );
    }


    public MarcajeDTO registrarSalida(Usuario usuario) {
        List<Marcaje> marcajes = marcajeRepository.findByUsuario(usuario);
        if (!marcajes.isEmpty()) {
            Marcaje ultimoMarcaje = marcajes.get(marcajes.size() - 1);
            if (ultimoMarcaje.getHoraSalida() == null) {
                Horario horarioUsuario = usuario.getHorario();
    
                ultimoMarcaje.setHoraSalida(LocalTime.now());
                ultimoMarcaje.setDentroDeHorario(verificarHorarioSalida(LocalTime.now(), horarioUsuario));
                Marcaje updatedMarcaje = marcajeRepository.save(ultimoMarcaje);
    
                // Mapear a MarcajeDTO
                return new MarcajeDTO(
                    updatedMarcaje.getIdMarcaje(),
                    usuario.getIdUsuario(),
                    usuario.getUsername(),
                    updatedMarcaje.getFechaMarcaje(),
                    updatedMarcaje.getHoraEntrada(),
                    updatedMarcaje.getHoraSalida(),
                    updatedMarcaje.getDentroDeHorario()
                );
            }
        }
        return null;
    }

    private MarcajeDTO convertirAMarcajeDTO(Marcaje marcaje) {
        return new MarcajeDTO(
                marcaje.getIdMarcaje(),
                marcaje.getUsuario().getIdUsuario(),
                marcaje.getUsuario().getUsername(),
                marcaje.getFechaMarcaje(),
                marcaje.getHoraEntrada(),
                marcaje.getHoraSalida(),
                marcaje.getDentroDeHorario()
        );
    }


    public List<MarcajeDTO> obtenerMarcajes(Usuario usuario) {
        return marcajeRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertirAMarcajeDTO)
                .collect(Collectors.toList());
    }


    public List<MarcajeDTO> obtenerTodosLosMarcajes() {
        return marcajeRepository.findAll()
                .stream()
                .map(this::convertirAMarcajeDTO)
                .collect(Collectors.toList());
    }


    public Page<MarcajeDTO> obtenerMarcajesPaginados(Pageable pageable) {
        Page<Marcaje> marcajesPage = marcajeRepository.findAll(pageable);
        List<MarcajeDTO> marcajesDTO = marcajesPage.getContent()
                .stream()
                .map(this::convertirAMarcajeDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(marcajesDTO, pageable, marcajesPage.getTotalElements());
    }


    public Optional<MarcajeDTO> obtenerListaDeMarcajes(Long idUsuario) {
        return marcajeRepository.findFirstByUsuarioIdUsuarioOrderByFechaMarcajeDesc(idUsuario)
                .map(this::convertirAMarcajeDTO);
    }


    private boolean verificarHorarioEntrada(LocalTime horaEntrada, Horario horario) {
        LocalTime horaLimiteEntrada = horario.getHoraEntrada().plusMinutes(horario.getToleranciaEntrada());
        return horaEntrada.isBefore(horaLimiteEntrada) || horaEntrada.equals(horaLimiteEntrada);
    }

    private boolean verificarHorarioSalida(LocalTime horaSalida, Horario horario) {
        LocalTime horaLimiteSalida = horario.getHoraSalida().minusMinutes(horario.getToleranciaSalida());
        return horaSalida.isAfter(horaLimiteSalida) || horaSalida.equals(horaLimiteSalida);
    }


    public List<MarcajeDTO> obtenerMarcajesPorDepartamento(Long idDepartamento) {
        return marcajeRepository.findByDepartamentoId(idDepartamento)
                .stream()
                .map(this::convertirAMarcajeDTO)
                .collect(Collectors.toList());
    }


    public List<MarcajeDTO> obtenerMarcajesFueraDeHorario() {
        return marcajeRepository.findMarcajesFueraDeHorario()
                .stream()
                .map(this::convertirAMarcajeDTO)
                .collect(Collectors.toList());
    }

    public List<Usuario> obtenerUsuariosFueraDeHorario() {
        return marcajeRepository.findMarcajesFueraDeHorario()
                .stream()
                .map(Marcaje::getUsuario)
                .distinct()
                .collect(Collectors.toList());
    }



    public Marcaje obtenerMarcajePorId(Long idMarcaje) {
        return marcajeRepository.findById(idMarcaje)
                .orElseThrow(() -> new RuntimeException("Marcaje no encontrado con id: " + idMarcaje));
    }




}
