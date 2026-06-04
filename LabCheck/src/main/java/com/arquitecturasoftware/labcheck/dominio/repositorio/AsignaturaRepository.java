package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    Optional<Asignatura> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}