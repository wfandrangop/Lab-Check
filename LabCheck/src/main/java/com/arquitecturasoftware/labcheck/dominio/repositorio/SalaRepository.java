package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findByNombreCodigo(String nombreCodigo);

    boolean existsByNombreCodigo(String nombreCodigo);
}
