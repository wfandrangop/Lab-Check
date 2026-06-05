package com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio;

import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.SalaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataSalaRepositorio extends JpaRepository<SalaJpaEntity, Long> {
    Optional<SalaJpaEntity> findByNombreCodigo(String nombreCodigo);
}
