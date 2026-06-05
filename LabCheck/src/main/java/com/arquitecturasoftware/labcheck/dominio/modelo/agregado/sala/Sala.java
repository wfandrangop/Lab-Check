package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala;

import com.arquitecturasoftware.labcheck.dominio.modelo.evento.ComputadorRegistrado;
import com.arquitecturasoftware.labcheck.dominio.modelo.evento.EventoDominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala {
    private final Long id;
    private final String nombreCodigo;
    private final List<Computador> computadores;
    private final List<EventoDominio> eventosRegistrados = new ArrayList<>();

    public Sala(Long id, String nombreCodigo) {
        if (nombreCodigo == null || nombreCodigo.isBlank()) {
            throw new IllegalArgumentException("El nombre/código de la sala no puede estar vacío.");
        }
        this.id = id;
        this.nombreCodigo = nombreCodigo;
        this.computadores = new ArrayList<>();
    }

    public Sala(Long id, String nombreCodigo, List<Computador> computadores) {
        if (nombreCodigo == null || nombreCodigo.isBlank()) {
            throw new IllegalArgumentException("El nombre/código de la sala no puede estar vacío.");
        }
        this.id = id;
        this.nombreCodigo = nombreCodigo;
        this.computadores = new ArrayList<>(computadores != null ? computadores : Collections.emptyList());
    }

    public void registrarComputador(String codigoUnico) {
        boolean existe = computadores.stream()
                .anyMatch(c -> c.getCodigoUnico().equalsIgnoreCase(codigoUnico));
        if (existe) {
            throw new IllegalStateException("El computador con código " + codigoUnico + " ya está registrado en esta sala.");
        }
        this.computadores.add(new Computador(codigoUnico));

        this.eventosRegistrados.add(new ComputadorRegistrado(this.id, codigoUnico));
    }

    public List<EventoDominio> extraerEventos() {
        List<EventoDominio> copia = new ArrayList<>(this.eventosRegistrados);
        this.eventosRegistrados.clear();
        return copia;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCodigo() {
        return nombreCodigo;
    }

    public List<Computador> getComputadores() {
        return Collections.unmodifiableList(computadores);
    }
}