package com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio;

import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.BloqueHorarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBloqueRepositorio extends JpaRepository<BloqueHorarioJpaEntity, Long> {
}
