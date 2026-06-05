package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.novedad;

import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.EstadoNovedad;
import java.time.LocalDate;

public class ReporteNovedad {
    private final Long id;
    private final String pcCodigo;
    private final Long estudianteId;
    private final String descripcion;
    private final LocalDate fechaReporte;
    private EstadoNovedad estado;

    public ReporteNovedad(Long id, String pcCodigo, Long estudianteId, String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del inconveniente técnico no puede estar vacía.");
        }
        if (pcCodigo == null || pcCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de la PC no puede estar vacío.");
        }
        if (estudianteId == null) {
            throw new IllegalArgumentException("El estudianteId no puede ser nulo.");
        }
        this.id = id;
        this.pcCodigo = pcCodigo;
        this.estudianteId = estudianteId;
        this.descripcion = descripcion;
        this.fechaReporte = LocalDate.now();
        this.estado = EstadoNovedad.PENDIENTE;
    }

    public ReporteNovedad(Long id, String pcCodigo, Long estudianteId, String descripcion, LocalDate fechaReporte, EstadoNovedad estado) {
        this.id = id;
        this.pcCodigo = pcCodigo;
        this.estudianteId = estudianteId;
        this.descripcion = descripcion;
        this.fechaReporte = fechaReporte;
        this.estado = estado;
    }

    public void actualizarEstado(EstadoNovedad nuevoEstado) {
        if (this.estado == EstadoNovedad.SOLUCIONADO && nuevoEstado == EstadoNovedad.EN_REPARACION) {
            throw new IllegalStateException("No se puede reparar un equipo que ya ha sido marcado como SOLUCIONADO.");
        }
        this.estado = nuevoEstado;
    }

    public Long getId() { return id; }
    public String getPcCodigo() { return pcCodigo; }
    public Long getEstudianteId() { return estudianteId; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaReporte() { return fechaReporte; }
    public EstadoNovedad getEstado() { return estado; }
}
