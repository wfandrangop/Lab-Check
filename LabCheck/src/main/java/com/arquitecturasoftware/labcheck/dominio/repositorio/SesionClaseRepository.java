package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SesionClaseRepository extends JpaRepository<SesionClase, Long> {

    List<SesionClase> findByActivaTrue();

    List<SesionClase> findByProfesor(Usuario profesor);

    List<SesionClase> findByProfesorAndActivaTrue(Usuario profesor);
}
