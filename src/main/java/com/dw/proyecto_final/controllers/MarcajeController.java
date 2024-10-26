package com.dw.proyecto_final.controllers;


import com.dw.proyecto_final.dtos.MarcajeDTO;
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


/*     @PostMapping("/salida/{username}")
        public ResponseEntity<?> registrarSalida(@PathVariable String username) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            MarcajeDTO marcajeDTO = marcajeService.registrarSalida(usuario);
        return marcajeDTO != null 
            ? new ResponseEntity<>(marcajeDTO, HttpStatus.CREATED)
            : new ResponseEntity<>("El usuario no tiene una entrada sin salida", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    } */

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





    @GetMapping("/historial/{username}")
    public ResponseEntity<?> obtenerMarcajes(@PathVariable String username) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            List<Marcaje> marcajes = marcajeService.obtenerMarcajes(usuario);
            return new ResponseEntity<>(marcajes, HttpStatus.OK);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }


/*     @GetMapping("/")
    public List<Marcaje> obtenerTodosLosMarcajes(){

        return marcajeService.obtenerTodosLosMarcajes();
    } */

    @GetMapping("/")
    public ResponseEntity<List<Marcaje>> obtenerTodosLosMarcajes() {
        List<Marcaje> marcajes = marcajeService.obtenerTodosLosMarcajes();
        return new ResponseEntity<>(marcajes, HttpStatus.OK);
    }


/*     @GetMapping("/paginados/")
    public Page<Marcaje> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return marcajeService.obtenerMarcajesPaginados(pageable);
    } */

    @GetMapping("/paginados")
    public ResponseEntity<Page<Marcaje>> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Marcaje> marcajes = marcajeService.obtenerMarcajesPaginados(pageable);
        return new ResponseEntity<>(marcajes, HttpStatus.OK);
    }

/*     @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<List<Marcaje>> obtenerMarcajesPorDepartamento(@PathVariable Long idDepartamento) {
        List<Marcaje> marcajes = marcajeService.obtenerMarcajesPorDepartamento(idDepartamento);
        return ResponseEntity.ok(marcajes);
    } */

    @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<List<Marcaje>> obtenerMarcajesPorDepartamento(@PathVariable Long idDepartamento) {
        List<Marcaje> marcajes = marcajeService.obtenerMarcajesPorDepartamento(idDepartamento);
        return marcajes.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(marcajes, HttpStatus.OK);
    }


/*     @GetMapping("/fuera/horario/todos")
    public ResponseEntity<List<Marcaje>> obtenerMarcajesFueraDeHorario() {
        List<Marcaje> marcajes = marcajeService.obtenerMarcajesFueraDeHorario();
        return ResponseEntity.ok(marcajes);
    } */


    @GetMapping("/fuera/horario/todos")
    public ResponseEntity<List<Marcaje>> obtenerMarcajesFueraDeHorario() {
        List<Marcaje> marcajes = marcajeService.obtenerMarcajesFueraDeHorario();
        return marcajes.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(marcajes, HttpStatus.OK);
    }

/*     @GetMapping("/fuera/horario")
    public List<Usuario> obtenerUsuariosFueraDeHorario() {
        return marcajeService.obtenerUsuariosFueraDeHorario();
    } */

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

        // Crear un DTO con los datos específicos
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
