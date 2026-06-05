package com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record RangoTiempo(LocalDateTime horaInicio, LocalDateTime horaFin) {
    public RangoTiempo {
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
        }
        if (horaInicio.isAfter(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la hora de fin.");
        }

        if (horaInicio.plusHours(2).isBefore(horaFin)) {
            throw new IllegalArgumentException("Un bloque horario no puede exceder las 2 horas de duración.");
        }
        LocalTime inicioLimit = LocalTime.of(8, 0);
        LocalTime finLimit = LocalTime.of(20, 0);
        if (horaInicio.toLocalTime().isBefore(inicioLimit) || horaFin.toLocalTime().isAfter(finLimit)) {
            throw new IllegalArgumentException("Los bloques de laboratorio solo están permitidos entre las 08:00 AM y 20:00 PM.");
        }
    }

    public boolean incluye(LocalDateTime momento) {
        return !momento.isBefore(horaInicio) && !momento.isAfter(horaFin);
    }
}
