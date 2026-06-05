package com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor;

public record CorreoInstitucional(String value) {
    public CorreoInstitucional {
        if (value == null || !value.endsWith("@uce.edu.ec")) {
            throw new IllegalArgumentException("El correo debe ser un correo institucional válido de la UCE.");
        }
    }
}
