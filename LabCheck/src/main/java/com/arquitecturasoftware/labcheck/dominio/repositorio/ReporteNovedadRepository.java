package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.novedad.ReporteNovedad;
import java.util.Optional;
import java.util.List;

public interface ReporteNovedadRepository {
    ReporteNovedad save(ReporteNovedad reporte);
    Optional<ReporteNovedad> findById(Long id);
    List<ReporteNovedad> findAll();
}
