package com.dw.proyecto_final.controllers;


import com.dw.proyecto_final.dtos.MarcajeDTO;
import com.dw.proyecto_final.dtos.UsuarioDTO;
import com.dw.proyecto_final.models.Marcaje;
import com.dw.proyecto_final.models.Usuario;
import com.dw.proyecto_final.services.MarcajeService;
import com.dw.proyecto_final.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/marcajes")
@CrossOrigin("http://localhost:4200")
public class MarcajeController {

    @Autowired
    MarcajeService marcajeService;

    @Autowired
    UsuarioService usuarioService;



    @PostMapping("/entrada/{idUsuario}")
    public ResponseEntity<MarcajeDTO> registrarEntrada(@PathVariable Long idUsuario) {
        MarcajeDTO marcajeDTO = marcajeService.registrarEntrada(idUsuario);
        return ResponseEntity.ok(marcajeDTO);
    }



    @PostMapping("/salida/{idUsuario}")
        public ResponseEntity<?> registrarSalida(@PathVariable Long idUsuario) {
    Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(idUsuario); // Asegúrate de tener este método en tu servicio
    if (usuarioOptional.isPresent()) {
        Usuario usuario = usuarioOptional.get();
        MarcajeDTO marcajeDTO = marcajeService.registrarSalida(usuario);
        return marcajeDTO != null 
            ? new ResponseEntity<>(marcajeDTO, HttpStatus.CREATED)
            : new ResponseEntity<>("El usuario no tiene una entrada sin salida", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
}


    @GetMapping("/historial/username/{username}")
    public ResponseEntity<?> obtenerMarcajes(@PathVariable String username) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            List<MarcajeDTO> marcajes = marcajeService.obtenerMarcajes(usuario);
            return new ResponseEntity<>(marcajes, HttpStatus.OK);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/historial/id/{id}")
    public ResponseEntity<?> obtenerHistorialMarcajes(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>("Usuario con ID " + id + " no encontrado", HttpStatus.NOT_FOUND);
        }

        List<MarcajeDTO> marcajes = marcajeService.obtenerMarcajesPorUsuarioId(id);

        if (marcajes.isEmpty()) {
            return new ResponseEntity<>("No se encontraron marcajes para el usuario con ID " + id, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(marcajes, HttpStatus.OK);
    }



    @GetMapping("/")
    public ResponseEntity<List<MarcajeDTO>> obtenerTodosLosMarcajes() {
        List<MarcajeDTO> marcajes = marcajeService.obtenerTodosLosMarcajes();
        return new ResponseEntity<>(marcajes, HttpStatus.OK);
    }


    @GetMapping("/paginados")
    public ResponseEntity<List<MarcajeDTO>> obtenerMarcajesPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MarcajeDTO> marcajesPage = marcajeService.obtenerMarcajesPaginados(pageable);

        List<MarcajeDTO> marcajes = marcajesPage.getContent();

        return marcajes.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(marcajes, HttpStatus.OK);
    }



    @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<List<MarcajeDTO>> obtenerMarcajesPorDepartamento(@PathVariable Long idDepartamento) {
        List<MarcajeDTO> marcajes = marcajeService.obtenerMarcajesPorDepartamento(idDepartamento);
        return marcajes.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(marcajes, HttpStatus.OK);
    }



    @GetMapping("/fuera/horario/todos")
    public ResponseEntity<List<MarcajeDTO>> obtenerMarcajesFueraDeHorario() {
        List<MarcajeDTO> marcajes = marcajeService.obtenerMarcajesFueraDeHorario();
        return marcajes.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(marcajes, HttpStatus.OK);
    }


    @GetMapping("/fuera/horario")
    public ResponseEntity<List<Usuario>> obtenerUsuariosFueraDeHorario() {
        List<Usuario> usuariosFueraHorario = marcajeService.obtenerUsuariosFueraDeHorario();
        return usuariosFueraHorario.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(usuariosFueraHorario, HttpStatus.OK);
    }

    @GetMapping("/marcaje/{id}")
        public ResponseEntity<MarcajeDTO> getMarcaje(@PathVariable Long id) {
        Marcaje marcaje = marcajeService.obtenerMarcajePorId(id);

        MarcajeDTO marcajeDTO = new MarcajeDTO();
        marcajeDTO.setIdMarcaje(marcaje.getIdMarcaje());
        marcajeDTO.setIdUsuario(marcaje.getUsuario().getIdUsuario());
        marcajeDTO.setUsername(marcaje.getUsuario().getUsername());
        marcajeDTO.setFechaMarcaje(marcaje.getFechaMarcaje());
        marcajeDTO.setHoraEntrada(marcaje.getHoraEntrada());
        marcajeDTO.setHoraSalida(marcaje.getHoraSalida());
        marcajeDTO.setDentroDeHorario(marcaje.getDentroDeHorario());

    return ResponseEntity.ok(marcajeDTO);
}




}
