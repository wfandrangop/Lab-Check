package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Asistencia;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    boolean existsBySesionClaseAndEstudiante(SesionClase sesionClase, Usuario estudiante);
    List<Asistencia> findBySesionClase(SesionClase sesionClase);
    List<Asistencia> findByEstudiante(Usuario estudiante);
}
