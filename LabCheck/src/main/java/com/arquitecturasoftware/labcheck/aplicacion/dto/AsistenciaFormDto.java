package com.arquitecturasoftware.labcheck.aplicacion.dto;

public class AsistenciaFormDto {
    private Long bloqueId;
    private Long estudianteId;
    private String pcCodigo;
    private String observaciones;

    public AsistenciaFormDto() {
    }

    public AsistenciaFormDto(Long bloqueId, Long estudianteId, String pcCodigo, String observaciones) {
        this.bloqueId = bloqueId;
        this.estudianteId = estudianteId;
        this.pcCodigo = pcCodigo;
        this.observaciones = observaciones;
    }

    public Long getBloqueId() {
        return bloqueId;
    }

    public void setBloqueId(Long bloqueId) {
        this.bloqueId = bloqueId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getPcCodigo() {
        return pcCodigo;
    }

    public void setPcCodigo(String pcCodigo) {
        this.pcCodigo = pcCodigo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
