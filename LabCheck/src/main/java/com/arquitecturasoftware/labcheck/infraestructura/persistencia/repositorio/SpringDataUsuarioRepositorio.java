package com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio;

import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUsuarioRepositorio extends JpaRepository<UsuarioJpaEntity, Long> {
    Optional<UsuarioJpaEntity> findByCorreoInstitucional(String correoInstitucional);

    Optional<UsuarioJpaEntity> findByCedula(String cedula);
}
