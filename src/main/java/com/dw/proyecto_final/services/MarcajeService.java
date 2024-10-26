package com.dw.proyecto_final.services;

import com.dw.proyecto_final.dtos.MarcajeDTO;
import com.dw.proyecto_final.models.Horario;
import com.dw.proyecto_final.models.Marcaje;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.repositories.MarcajeRepository;
import com.dw.proyecto_final.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    // Mapear a MarcajeDTO
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


/*     public Marcaje registrarSalida(Usuario usuario){
        List<Marcaje> marcajes = marcajeRepository.findByUsuario(usuario);
        if(!marcajes.isEmpty()){
            Marcaje ultimoMarcaje = marcajes.get(marcajes.size()-1);
            if(ultimoMarcaje.getHoraSalida() == null){
                Horario horarioUsuario = usuario.getHorario();

                ultimoMarcaje.setHoraSalida(LocalTime.now());
                ultimoMarcaje.setDentroDeHorario(verificarHorarioSalida(LocalTime.now(), horarioUsuario));
                return marcajeRepository.save(ultimoMarcaje);
            }
        }
        return  null;
    } */

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
    

    public List<Marcaje> obtenerMarcajes(Usuario usuario){
        return marcajeRepository.findByUsuario(usuario);
    }


//    public Marcaje obtenerUltimoMarcaje(Usuario usuario) {
//        return marcajeRepository.findTopByUsuarioOrderByIdDesc(usuario);
//    }


    public List<Marcaje> obtenerTodosLosMarcajes() {
        return marcajeRepository.findAll();
    }

    public Page<Marcaje> obtenerMarcajesPaginados(Pageable pageable){
        return marcajeRepository.findAll(pageable);
    }

    public Optional<Marcaje> obtenerListaDeMarcajes(Long idUsuario) {
        return marcajeRepository.findFirstByUsuarioIdUsuarioOrderByFechaMarcajeDesc(idUsuario);
    }


    private boolean verificarHorarioEntrada(LocalTime horaEntrada, Horario horario) {
        LocalTime horaLimiteEntrada = horario.getHoraEntrada().plusMinutes(horario.getToleranciaEntrada());
        return horaEntrada.isBefore(horaLimiteEntrada) || horaEntrada.equals(horaLimiteEntrada);
    }

    private boolean verificarHorarioSalida(LocalTime horaSalida, Horario horario) {
        LocalTime horaLimiteSalida = horario.getHoraSalida().minusMinutes(horario.getToleranciaSalida());
        return horaSalida.isAfter(horaLimiteSalida) || horaSalida.equals(horaLimiteSalida);
    }

    public List<Marcaje> obtenerMarcajesPorDepartamento(Long idDepartamento) {
        return marcajeRepository.findByDepartamentoId(idDepartamento);
    }

    public List<Marcaje> obtenerMarcajesFueraDeHorario() {
        return marcajeRepository.findMarcajesFueraDeHorario();
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
