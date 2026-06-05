package com.arquitecturasoftware.labcheck.dominio.modelo.evento;

import java.time.LocalDateTime;

public record AsistenciaHabilitada(Long bloqueId, LocalDateTime ocurridoEn) implements EventoDominio {
    public AsistenciaHabilitada(Long bloqueId) {
        this(bloqueId, LocalDateTime.now());
    }
}