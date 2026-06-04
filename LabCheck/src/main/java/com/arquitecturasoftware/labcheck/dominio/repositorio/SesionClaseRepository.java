package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SesionClaseRepository extends JpaRepository<SesionClase, Long> {

    List<SesionClase> findByActivaTrue();

    List<SesionClase> findByProfesor(Usuario profesor);

    @Query("SELECT s FROM SesionClase s WHERE s.sala = :sala AND s.activa = true AND s.horaInicio < :horaFin AND s.horaFin > :horaInicio")
    List<SesionClase> findOverlappingSessions(@Param("sala") Sala sala, @Param("horaInicio") LocalDateTime horaInicio, @Param("horaFin") LocalDateTime horaFin);
}
