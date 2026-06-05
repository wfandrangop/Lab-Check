package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.RangoTiempo;
import com.arquitecturasoftware.labcheck.dominio.repositorio.BloqueHorarioRepository;
import com.arquitecturasoftware.labcheck.infraestructura.evento.PublicadorEventosExpress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BloqueHorarioAppService {
    private final BloqueHorarioRepository bloqueHorarioRepository;

    public BloqueHorarioAppService(BloqueHorarioRepository bloqueHorarioRepository) {
        this.bloqueHorarioRepository = bloqueHorarioRepository;
    }

    public void abrirBloque(Long bloqueId, Long salaId, Long profesorId, Long asignaturaId, RangoTiempo rangoTiempo) {
        BloqueHorario bloque = new BloqueHorario(bloqueId, salaId, profesorId, asignaturaId, rangoTiempo);
        bloqueHorarioRepository.save(bloque);
        PublicadorEventosExpress.publicar(bloque.extraerEventos());
    }

    public void cerrarBloque(Long bloqueId) {
        BloqueHorario bloque = bloqueHorarioRepository.findById(bloqueId)
                .orElseThrow(() -> new IllegalArgumentException("El bloque horario con ID " + bloqueId + " no existe."));

        bloque.cerrarManualmente();

        bloqueHorarioRepository.save(bloque);

        PublicadorEventosExpress.publicar(bloque.extraerEventos());
    }

    public List<BloqueHorario> obtenerTodosBloques() {
        return bloqueHorarioRepository.findAll();
    }

    public void habilitarAsistenciaDeBloque(Long bloqueId) {
        BloqueHorario bloque = bloqueHorarioRepository.findById(bloqueId)
                .orElseThrow(() -> new IllegalArgumentException("El bloque con ID " + bloqueId + " no existe."));

        // Cambia el estado a ACTIVO en el dominio y genera el evento AsistenciaHabilitada
        bloque.habilitarAsistencia();

        bloqueHorarioRepository.save(bloque);

        // Publica el evento hacia la infraestructura
        PublicadorEventosExpress.publicar(bloque.extraerEventos());
    }
}
