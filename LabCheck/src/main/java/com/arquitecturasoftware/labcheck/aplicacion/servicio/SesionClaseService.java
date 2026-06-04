package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Asignatura;
import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SesionClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionClaseService {

    private final SesionClaseRepository sesionClaseRepository;

    @Transactional
    public SesionClase abrirLaboratorio(Usuario profesor, Sala sala, Asignatura asignatura) {
        SesionClase sesion = SesionClase.builder()
                .sala(sala)
                .profesor(profesor)
                .asignatura(asignatura)
                .horaInicio(LocalDateTime.now())
                .horaFin(LocalDateTime.now().plusHours(2))
                .activa(true)
                .build();
        return sesionClaseRepository.save(sesion);
    }

    /**
     * Cierra manualmente una sesión de clase desactivando el flag.
     */
    @Transactional
    public void cerrarSesion(Long sesionId) {
        SesionClase sesion = sesionClaseRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + sesionId));
        sesion.setActiva(false);
        sesionClaseRepository.save(sesion);
    }

    /**
     * Retorna todas las sesiones activas (para vista de estudiantes).
     */
    @Transactional(readOnly = true)
    public List<SesionClase> obtenerSesionesActivas() {
        return sesionClaseRepository.findByActivaTrue();
    }

    /**
     * Retorna las sesiones del profesor autenticado.
     */
    @Transactional(readOnly = true)
    public List<SesionClase> obtenerSesionesPorProfesor(Usuario profesor) {
        return sesionClaseRepository.findByProfesor(profesor);
    }

    /**
     * Retorna todas las sesiones (para dashboard del administrador).
     */
    @Transactional(readOnly = true)
    public List<SesionClase> obtenerTodasLasSesiones() {
        return sesionClaseRepository.findAll();
    }

    /**
     * Busca una sesión por su ID.
     */
    @Transactional(readOnly = true)
    public SesionClase obtenerSesionPorId(Long id) {
        return sesionClaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + id));
    }
}
