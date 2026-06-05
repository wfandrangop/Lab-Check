package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque;

import java.time.LocalDateTime;

public class Asistencia {
    private final Long id;
    private final Long estudianteId;
    private final String pcCodigo;
    private final LocalDateTime fechaRegistro;

    public Asistencia(Long id, Long estudianteId, String pcCodigo, LocalDateTime fechaRegistro) {
        if (estudianteId == null) {
            throw new IllegalArgumentException("El ID del estudiante no puede ser nulo.");
        }
        if (pcCodigo == null || pcCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de la PC no puede ser nulo o vacío.");
        }
        if (fechaRegistro == null) {
            throw new IllegalArgumentException("La fecha de registro no puede ser nula.");
        }
        this.id = id;
        this.estudianteId = estudianteId;
        this.pcCodigo = pcCodigo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public Long getEstudianteId() { return estudianteId; }
    public String getPcCodigo() { return pcCodigo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
