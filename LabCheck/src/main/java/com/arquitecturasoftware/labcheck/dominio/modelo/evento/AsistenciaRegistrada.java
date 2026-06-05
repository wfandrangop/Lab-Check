package com.arquitecturasoftware.labcheck.dominio.modelo.evento;

import java.time.LocalDateTime;

public record AsistenciaRegistrada(Long bloqueId, Long estudianteId, String pcCodigo, LocalDateTime ocurridoEn) implements EventoDominio {}
