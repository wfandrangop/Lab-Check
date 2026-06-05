package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.novedad.ReporteNovedad;
import com.arquitecturasoftware.labcheck.dominio.repositorio.BloqueHorarioRepository;
import com.arquitecturasoftware.labcheck.dominio.servicio.VerificacionAsistenciaDomainService;
import com.arquitecturasoftware.labcheck.infraestructura.evento.PublicadorEventosExpress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.arquitecturasoftware.labcheck.dominio.repositorio.ReporteNovedadRepository;

@Service
@Transactional
public class AsistenciaAppService {
    private final BloqueHorarioRepository bloqueHorarioRepository;
    private final VerificacionAsistenciaDomainService verificacionAsistenciaDomainService;
    private final ReporteNovedadRepository novedadRepository;

    public AsistenciaAppService(BloqueHorarioRepository bloqueHorarioRepository, VerificacionAsistenciaDomainService verificacionAsistenciaDomainService, ReporteNovedadRepository novedadRepository) {
        this.bloqueHorarioRepository = bloqueHorarioRepository;
        this.verificacionAsistenciaDomainService = verificacionAsistenciaDomainService;
        this.novedadRepository = novedadRepository;
    }

    public void registrarAsistencia(Long bloqueId, Long asistenciaId, Long estudianteId, String pcCodigo, String observaciones) {
        BloqueHorario bloque = bloqueHorarioRepository.findById(bloqueId).orElseThrow(() -> new IllegalArgumentException("El bloque horario con ID " + bloqueId + " no existe."));

        verificacionAsistenciaDomainService.verificarYRegistrar(bloque, asistenciaId, estudianteId, pcCodigo);

        if (observaciones != null && !observaciones.isBlank()) {
            Long novedadId = System.nanoTime();
            ReporteNovedad novedad = new ReporteNovedad(novedadId, pcCodigo, estudianteId, observaciones);
            novedadRepository.save(novedad);
        }

        bloqueHorarioRepository.save(bloque);

        PublicadorEventosExpress.publicar(bloque.extraerEventos());
    }
}
