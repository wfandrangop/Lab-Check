package com.arquitecturasoftware.labcheck.infraestructura.persistencia.adaptador;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.Asistencia;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.EstadoBloque;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.RangoTiempo;
import com.arquitecturasoftware.labcheck.dominio.repositorio.BloqueHorarioRepository;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.AsistenciaJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.BloqueHorarioJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio.SpringDataBloqueRepositorio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BloqueHorarioRepositoryImpl implements BloqueHorarioRepository {

    private final SpringDataBloqueRepositorio springDataRepository;

    public BloqueHorarioRepositoryImpl(SpringDataBloqueRepositorio springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public BloqueHorario save(BloqueHorario bloqueHorario) {
        BloqueHorarioJpaEntity entity = toEntity(bloqueHorario);
        if (entity.getAsistencias() != null) {
            for (AsistenciaJpaEntity a : entity.getAsistencias()) {
                a.setBloque(entity);
            }
        }
        BloqueHorarioJpaEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<BloqueHorario> findById(Long id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<BloqueHorario> findAll() {
        return springDataRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private BloqueHorarioJpaEntity toEntity(BloqueHorario domain) {
        BloqueHorarioJpaEntity entity = new BloqueHorarioJpaEntity();
        entity.setId(domain.getId());
        entity.setSalaId(domain.getSalaId());
        entity.setProfesorId(domain.getProfesorId());
        entity.setAsignaturaId(domain.getAsignaturaId());
        entity.setHoraInicio(domain.getRangoTiempo().horaInicio());
        entity.setHoraFin(domain.getRangoTiempo().horaFin());
        entity.setEstado(domain.getEstado().name());

        List<AsistenciaJpaEntity> asisList = domain.getAsistencias().stream()
                .map(a -> new AsistenciaJpaEntity(a.getId(), a.getEstudianteId(), a.getPcCodigo(), a.getFechaRegistro(), entity))
                .collect(Collectors.toList());
        entity.setAsistencias(asisList);
        return entity;
    }

    private BloqueHorario toDomain(BloqueHorarioJpaEntity entity) {
        RangoTiempo rango = new RangoTiempo(entity.getHoraInicio(), entity.getHoraFin());
        List<Asistencia> asisList = entity.getAsistencias().stream()
                .map(a -> new Asistencia(a.getId(), a.getEstudianteId(), a.getPcCodigo(), a.getFechaRegistro()))
                .collect(Collectors.toList());

        EstadoBloque estado = EstadoBloque.valueOf(entity.getEstado());
        return new BloqueHorario(
                entity.getId(),
                entity.getSalaId(),
                entity.getProfesorId(),
                entity.getAsignaturaId(),
                rango,
                estado,
                asisList
        );
    }
}
