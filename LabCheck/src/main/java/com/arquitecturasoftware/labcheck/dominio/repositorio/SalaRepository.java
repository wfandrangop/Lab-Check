package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala.Sala;
import java.util.Optional;
import java.util.List;

public interface SalaRepository {
    Sala save(Sala sala);
    Optional<Sala> findById(Long id);
    Optional<Sala> findByNombreCodigo(String nombreCodigo);
    List<Sala> findAll();
}
