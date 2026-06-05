package com.arquitecturasoftware.labcheck.dominio.modelo.evento;

import java.time.LocalDateTime;

public record ComputadorRegistrado(Long salaId, String pcCodigo, LocalDateTime ocurridoEn) implements EventoDominio {
    public ComputadorRegistrado(Long salaId, String pcCodigo) {
        this(salaId, pcCodigo, LocalDateTime.now());
    }
}