package com.dw.proyecto_final.repositories;

import com.dw.proyecto_final.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreRol(String nombreRol);
    Optional<Rol> findAllById(Long idRol);
}
