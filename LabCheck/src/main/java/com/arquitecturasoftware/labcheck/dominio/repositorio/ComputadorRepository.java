package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Computador;
import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComputadorRepository extends JpaRepository<Computador, Long> {
    Optional<Computador> findByCodigoUnicoAndSala(String codigoUnico, Sala sala);

    boolean existsByCodigoUnico(String codigoUnico);
}
