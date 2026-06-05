package com.arquitecturasoftware.labcheck.dominio.modelo.evento;

import java.time.LocalDateTime;

public record BloqueHorarioCerrado(Long bloqueId, String motivo, LocalDateTime ocurridoEn) implements EventoDominio {}
