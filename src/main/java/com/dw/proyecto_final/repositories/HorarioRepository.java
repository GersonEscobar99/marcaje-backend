package com.dw.proyecto_final.repositories;

import com.dw.proyecto_final.models.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario,Long> {

}
