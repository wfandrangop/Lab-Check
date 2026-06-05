package com.arquitecturasoftware.labcheck.infraestructura.evento;

import com.arquitecturasoftware.labcheck.dominio.modelo.evento.EventoDominio;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PublicadorEventosExpress {
    private static final List<Consumer<EventoDominio>> suscriptores = new ArrayList<>();

    public static void suscribir(Consumer<EventoDominio> suscriptor) {
        suscriptores.add(suscriptor);
    }

    public static void publicar(List<EventoDominio> eventos) {
        eventos.forEach(evento -> suscriptores.forEach(s -> s.accept(evento)));
    }
}
