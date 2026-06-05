package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala.Sala;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SalaRepository;
import com.arquitecturasoftware.labcheck.infraestructura.evento.PublicadorEventosExpress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistroComputadoresAppService {
    private final SalaRepository salaRepository;

    public RegistroComputadoresAppService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    // CASO DE USO: El administrador registra computadores mediante el servicio dedicado
    public void registrarComputadorEnSala(Long salaId, String pcCodigo) {
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("La sala de laboratorio con ID " + salaId + " no existe."));

        // Ejecuta la regla de negocio del dominio
        sala.registrarComputador(pcCodigo);

        // Persistencia de los cambios
        salaRepository.save(sala);

        // Publicación de eventos tácticos (ComputadorRegistrado)
        PublicadorEventosExpress.publicar(sala.extraerEventos());
    }
}