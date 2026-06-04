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

    /**
     * Registra la asistencia de un estudiante aplicando las reglas de negocio:
     * 1. Verifica que la sesión esté activa y dentro del horario.
     * 2. Valida que la PC pertenezca a la sala de la sesión.
     * 3. Evita registros duplicados del mismo estudiante en la misma sesión.
     * 4. Si hay novedades, las persiste como Inconveniente.
     */
    @Transactional
    public void registrarAsistenciaEstudiante(Usuario estudiante, Long sesionId,
                                               String pcCodigo, String novedades) {
        // 1. Validar el candado de negocio: ¿La sesión está activa?
        SesionClase sesion = sesionClaseRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada."));

        if (!sesion.isActiva() || LocalDateTime.now().isAfter(sesion.getHoraFin())) {
            throw new IllegalStateException(
                    "El registro de asistencia está cerrado para este laboratorio.");
        }

        // 2. Verificar que el estudiante no haya registrado ya en esta sesión
        if (asistenciaRepository.existsBySesionClaseAndEstudiante(sesion, estudiante)) {
            throw new IllegalStateException(
                    "Ya registraste tu asistencia en esta sesión.");
        }

        // 3. Buscar y vincular la PC física a la sala de la sesión
        Computador pc = computadorRepository.findByCodigoUnicoAndSala(pcCodigo, sesion.getSala())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La PC '" + pcCodigo + "' no pertenece a este laboratorio."));

        // 4. Persistir la asistencia
        Asistencia asistencia = Asistencia.builder()
                .sesionClase(sesion)
                .estudiante(estudiante)
                .computador(pc)
                .fechaRegistro(LocalDateTime.now())
                .build();
        asistenciaRepository.save(asistencia);

        // 5. Si el estudiante reportó novedades, persistirlas como Inconveniente
        if (novedades != null && !novedades.trim().isEmpty()) {
            Inconveniente inconveniente = Inconveniente.builder()
                    .computador(pc)
                    .estudiante(estudiante)
                    .descripcion(novedades.trim())
                    .fechaReporte(LocalDate.now())
                    .build();
            inconvenienteRepository.save(inconveniente);
        }
    }

    /**
     * Obtiene las asistencias registradas en una sesión específica.
     */
    @Transactional(readOnly = true)
    public List<Asistencia> obtenerAsistenciasPorSesion(SesionClase sesion) {
        return asistenciaRepository.findBySesionClase(sesion);
    }

    /**
     * Obtiene todas las asistencias (para dashboard del administrador).
     */
    @Transactional(readOnly = true)
    public List<Asistencia> obtenerTodasLasAsistencias() {
        return asistenciaRepository.findAll();
    }

    /**
     * Obtiene todos los inconvenientes reportados.
     */
    @Transactional(readOnly = true)
    public List<Inconveniente> obtenerTodosLosInconvenientes() {
        return inconvenienteRepository.findAll();
    }
}
