package com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio;

import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.ReporteNovedadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReporteNovedadRepositorio extends JpaRepository<ReporteNovedadJpaEntity, Long> {
}
