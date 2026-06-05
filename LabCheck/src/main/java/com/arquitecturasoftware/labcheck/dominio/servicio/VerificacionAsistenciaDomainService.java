package com.arquitecturasoftware.labcheck.dominio.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala.Sala;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SalaRepository;
import java.time.LocalDateTime;

public class VerificacionAsistenciaDomainService {
    private final SalaRepository salaRepository;

    public VerificacionAsistenciaDomainService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public void verificarYRegistrar(BloqueHorario bloque, Long asistenciaId, Long estudianteId, String pcCodigo) {
        Sala sala = salaRepository.findById(bloque.getSalaId())
            .orElseThrow(() -> new IllegalArgumentException("La sala asociada al bloque no existe."));
            
        boolean pcExisteEnSala = sala.getComputadores().stream()
            .anyMatch(pc -> pc.getCodigoUnico().equalsIgnoreCase(pcCodigo));
            
        if (!pcExisteEnSala) {
            throw new IllegalArgumentException("Operación Inválida: La computadora " + pcCodigo + " no pertenece físicamente a la sala " + sala.getNombreCodigo());
        }

        bloque.registrarAsistencia(asistenciaId, estudianteId, pcCodigo, LocalDateTime.now());
    }
}
