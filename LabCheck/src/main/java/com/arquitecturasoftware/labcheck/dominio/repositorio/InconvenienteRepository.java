package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Computador;
import com.arquitecturasoftware.labcheck.dominio.modelo.Inconveniente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InconvenienteRepository extends JpaRepository<Inconveniente, Long> {
    List<Inconveniente> findByComputador(Computador computador);

    List<Inconveniente> findByFechaReporteBetween(LocalDate inicio, LocalDate fin);
}
