package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala;

public class Computador {
    private final String codigoUnico;

    public Computador(String codigoUnico) {
        if (codigoUnico == null || codigoUnico.isBlank()) {
            throw new IllegalArgumentException("El código de inventario de la PC no puede estar vacío.");
        }
        this.codigoUnico = codigoUnico;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }
}
