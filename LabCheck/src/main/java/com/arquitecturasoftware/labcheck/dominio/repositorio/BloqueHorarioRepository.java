package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import java.util.Optional;
import java.util.List;

public interface BloqueHorarioRepository {
    BloqueHorario save(BloqueHorario bloqueHorario);
    Optional<BloqueHorario> findById(Long id);
    List<BloqueHorario> findAll();
}
