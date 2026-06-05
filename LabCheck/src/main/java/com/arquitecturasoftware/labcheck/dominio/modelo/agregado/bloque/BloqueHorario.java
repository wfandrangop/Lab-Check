package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque;

import com.arquitecturasoftware.labcheck.dominio.modelo.evento.AsistenciaHabilitada;
import com.arquitecturasoftware.labcheck.dominio.modelo.evento.AsistenciaRegistrada;
import com.arquitecturasoftware.labcheck.dominio.modelo.evento.BloqueHorarioCerrado;
import com.arquitecturasoftware.labcheck.dominio.modelo.evento.EventoDominio;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.EstadoBloque;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.RangoTiempo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BloqueHorario {
    private final Long id;
    private final Long salaId;
    private final Long profesorId;
    private final Long asignaturaId;
    private final RangoTiempo rangoTiempo;
    private EstadoBloque estado;
    private final List<Asistencia> asistencias;
    private final List<EventoDominio> eventosRegistrados = new ArrayList<>();

    public BloqueHorario(Long id, Long salaId, Long profesorId, Long asignaturaId, RangoTiempo rangoTiempo) {
        this.id = id;
        this.salaId = salaId;
        this.profesorId = profesorId;
        this.asignaturaId = asignaturaId;
        this.rangoTiempo = rangoTiempo;
        // CAMBIO: Inicializa INACTIVO por defecto. Ya no lanza evento de activado en constructor.
        this.estado = EstadoBloque.INACTIVO;
        this.asistencias = new ArrayList<>();
    }

    // Constructor adicional para rehidratar desde persistencia con estado y asistencias
    public BloqueHorario(Long id, Long salaId, Long profesorId, Long asignaturaId, RangoTiempo rangoTiempo, EstadoBloque estado, List<Asistencia> asistencias) {
        this.id = id;
        this.salaId = salaId;
        this.profesorId = profesorId;
        this.asignaturaId = asignaturaId;
        this.rangoTiempo = rangoTiempo;
        this.estado = estado == null ? EstadoBloque.INACTIVO : estado;
        this.asistencias = new ArrayList<>();
        if (asistencias != null) {
            this.asistencias.addAll(asistencias);
        }
    }

    // NUEVO REQUERIMIENTO: El profesor habilita explícitamente el bloque horario
    public void habilitarAsistencia() {
        if (this.estado != EstadoBloque.INACTIVO) {
            throw new IllegalStateException("Solo se puede habilitar la asistencia de un bloque que esté INACTIVO.");
        }
        this.estado = EstadoBloque.ACTIVO;
        this.eventosRegistrados.add(new AsistenciaHabilitada(this.id));
    }

    public void cerrarManualmente() {
        this.estado = EstadoBloque.CERRADO_MANUAL;
        this.eventosRegistrados.add(new BloqueHorarioCerrado(this.id, "Cierre manual por el Profesor", LocalDateTime.now()));
    }

    public List<EventoDominio> extraerEventos() {
        List<EventoDominio> copia = new ArrayList<>(this.eventosRegistrados);
        this.eventosRegistrados.clear();
        return copia;
    }

    public void registrarAsistencia(Long asistenciaId, Long estudianteId, String pcCodigo, LocalDateTime momentoRegistro) {
        // Invariante 1: El bloque debe estar activo para recibir firmas
        if (this.estado != EstadoBloque.ACTIVO) {
            throw new IllegalStateException("Operación Inválida: El bloque horario no está ACTIVO o ya se encuentra cerrado.");
        }

        // Invariante 2: El momento del registro debe estar dentro del rango permitido
        if (!this.rangoTiempo.incluye(momentoRegistro)) {
            this.estado = EstadoBloque.EXPIRADO;
            throw new IllegalStateException("Operación Inválida: La ventana de tiempo para este bloque horario ha expirado.");
        }

        // Invariante 3: Evitar que el mismo estudiante registre doble asistencia en el mismo bloque
        boolean estudianteYaAsistio = this.asistencias.stream()
                .anyMatch(asistencia -> asistencia.getEstudianteId().equals(estudianteId));
        if (estudianteYaAsistio) {
            throw new IllegalStateException("Restricción: El estudiante con ID " + estudianteId + " ya registró su asistencia en este bloque.");
        }

        // Si pasa todas las invariantes, se muta el estado interno del agregado
        Asistencia nuevaAsistencia = new Asistencia(asistenciaId, estudianteId, pcCodigo, momentoRegistro);
        this.asistencias.add(nuevaAsistencia);

        // Se registra el evento de dominio para su posterior publicación reactiva
        this.eventosRegistrados.add(new AsistenciaRegistrada(this.id, estudianteId, pcCodigo, momentoRegistro));
    }

    public Long getId() {
        return id;
    }

    public Long getSalaId() {
        return salaId;
    }

    public Long getProfesorId() {
        return profesorId;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public RangoTiempo getRangoTiempo() {
        return rangoTiempo;
    }

    public EstadoBloque getEstado() {
        return estado;
    }

    public List<Asistencia> getAsistencias() {
        return Collections.unmodifiableList(asistencias);
    }
}