package com.arquitecturasoftware.labcheck.infraestructura.persistencia.adaptador;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.novedad.ReporteNovedad;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.EstadoNovedad;
import com.arquitecturasoftware.labcheck.dominio.repositorio.ReporteNovedadRepository;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.ReporteNovedadJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio.SpringDataReporteNovedadRepositorio;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReporteNovedadRepositoryImpl implements ReporteNovedadRepository {

    private final SpringDataReporteNovedadRepositorio springDataRepository;

    public ReporteNovedadRepositoryImpl(SpringDataReporteNovedadRepositorio springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReporteNovedad save(ReporteNovedad reporte) {
        ReporteNovedadJpaEntity entity = toEntity(reporte);
        ReporteNovedadJpaEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ReporteNovedad> findById(Long id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ReporteNovedad> findAll() {
        return springDataRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private ReporteNovedadJpaEntity toEntity(ReporteNovedad domain) {
        return new ReporteNovedadJpaEntity(
                domain.getId(),
                domain.getPcCodigo(),
                domain.getEstudianteId(),
                domain.getDescripcion(),
                domain.getFechaReporte(),
                domain.getEstado().name()
        );
    }

    private ReporteNovedad toDomain(ReporteNovedadJpaEntity entity) {
        return new ReporteNovedad(
                entity.getId(),
                entity.getPcCodigo(),
                entity.getEstudianteId(),
                entity.getDescripcion(),
                entity.getFechaReporte(),
                EstadoNovedad.valueOf(entity.getEstado())
        );
    }
}
