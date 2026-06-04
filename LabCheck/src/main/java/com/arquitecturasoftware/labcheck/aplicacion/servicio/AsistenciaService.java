package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.*;
import com.arquitecturasoftware.labcheck.dominio.repositorio.AsistenciaRepository;
import com.arquitecturasoftware.labcheck.dominio.repositorio.ComputadorRepository;
import com.arquitecturasoftware.labcheck.dominio.repositorio.InconvenienteRepository;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SesionClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final SesionClaseRepository sesionClaseRepository;
    private final ComputadorRepository computadorRepository;
    private final InconvenienteRepository inconvenienteRepository;

    @Transactional
    public void registrarAsistenciaEstudiante(Usuario estudiante, Long sesionId, String pcCodigo, String novedades) {

        SesionClase sesion = sesionClaseRepository.findById(sesionId).orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada."));

        if (!sesion.isActiva() || LocalDateTime.now().isAfter(sesion.getHoraFin())) {
            throw new IllegalStateException("El registro de asistencia está cerrado para este laboratorio.");
        }

        if (asistenciaRepository.existsBySesionClaseAndEstudiante(sesion, estudiante)) {
            throw new IllegalStateException("Ya registraste tu asistencia en esta sesión.");
        }

        List<Asistencia> asistenciasEstudiante = asistenciaRepository.findByEstudiante(estudiante);
        boolean registradoEnOtraActiva = asistenciasEstudiante.stream().anyMatch(a -> a.getSesionClase().isActiva());
        if (registradoEnOtraActiva) {
            throw new IllegalStateException("Ya has registrado tu asistencia en otra sesión de clase activa en este momento. Solo esa sesión es válida para ti.");
        }

        Computador pc = computadorRepository.findByCodigoUnicoAndSala(pcCodigo, sesion.getSala()).orElseThrow(() -> new IllegalArgumentException("La PC '" + pcCodigo + "' no pertenece a este laboratorio."));

        Asistencia asistencia = Asistencia.builder().sesionClase(sesion).estudiante(estudiante).computador(pc).fechaRegistro(LocalDateTime.now()).build();
        asistenciaRepository.save(asistencia);

        if (novedades != null && !novedades.trim().isEmpty()) {
            Inconveniente inconveniente = Inconveniente.builder().computador(pc).estudiante(estudiante).descripcion(novedades.trim()).fechaReporte(LocalDate.now()).build();
            inconvenienteRepository.save(inconveniente);
        }
    }


    @Transactional(readOnly = true)
    public List<Asistencia> obtenerAsistenciasPorSesion(SesionClase sesion) {
        return asistenciaRepository.findBySesionClase(sesion);
    }


    @Transactional(readOnly = true)
    public List<Asistencia> obtenerTodasLasAsistencias() {
        return asistenciaRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Inconveniente> obtenerTodosLosInconvenientes() {
        return inconvenienteRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Asistencia> obtenerAsistenciasPorEstudiante(Usuario estudiante) {
        return asistenciaRepository.findByEstudiante(estudiante);
    }
}
