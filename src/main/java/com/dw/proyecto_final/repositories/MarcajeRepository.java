package com.dw.proyecto_final.repositories;

import com.dw.proyecto_final.models.Marcaje;
import com.dw.proyecto_final.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarcajeRepository extends JpaRepository<Marcaje, Long> {

    List<Marcaje> findByUsuarioIdUsuario(Long idUsuario);

    List<Marcaje> findByUsuario(Usuario usuario);


    Optional<Marcaje> findFirstByUsuarioIdUsuarioOrderByFechaMarcajeDesc(Long idUsuario);


    @Query("SELECT m FROM Marcaje m WHERE m.usuario = :usuario AND m.fechaMarcaje = :fecha ORDER BY m.horaEntrada DESC")
    Optional<Marcaje> findUltimoMarcajeByUsuarioAndFechaMarcaje(@Param("usuario") Usuario usuario, @Param("fecha") LocalDate fecha);

    @Query("SELECT m FROM Marcaje m WHERE m.usuario.departamento.idDepartamento = :idDepartamento")
    List<Marcaje> findByDepartamentoId(@Param("idDepartamento") Long idDepartamento);

    @Query("SELECT m FROM Marcaje m WHERE m.dentroDeHorario = false")
    List<Marcaje> findMarcajesFueraDeHorario();

}
