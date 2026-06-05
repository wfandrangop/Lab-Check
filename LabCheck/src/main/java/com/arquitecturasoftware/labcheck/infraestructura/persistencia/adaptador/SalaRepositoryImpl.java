package com.arquitecturasoftware.labcheck.infraestructura.persistencia.adaptador;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala.Computador;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.sala.Sala;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SalaRepository;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.ComputadorJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.SalaJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio.SpringDataSalaRepositorio;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SalaRepositoryImpl implements SalaRepository {

    private final SpringDataSalaRepositorio springDataRepository;

    public SalaRepositoryImpl(SpringDataSalaRepositorio springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Sala save(Sala sala) {
        SalaJpaEntity entity = toEntity(sala);
        if (entity.getComputadores() != null) {
            for (ComputadorJpaEntity pc : entity.getComputadores()) {
                pc.setSala(entity);
            }
        }
        SalaJpaEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Sala> findById(Long id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Sala> findByNombreCodigo(String nombreCodigo) {
        return springDataRepository.findByNombreCodigo(nombreCodigo).map(this::toDomain);
    }

    @Override
    public List<Sala> findAll() {
        return springDataRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SalaJpaEntity toEntity(Sala sala) {
        SalaJpaEntity entity = new SalaJpaEntity();
        entity.setId(sala.getId());
        entity.setNombreCodigo(sala.getNombreCodigo());
        
        List<ComputadorJpaEntity> pcs = sala.getComputadores().stream()
                .map(pc -> new ComputadorJpaEntity(null, pc.getCodigoUnico(), entity))
                .collect(Collectors.toList());
        entity.setComputadores(pcs);
        return entity;
    }

    private Sala toDomain(SalaJpaEntity entity) {
        List<Computador> pcs = entity.getComputadores().stream()
                .map(pc -> new Computador(pc.getCodigoUnico()))
                .collect(Collectors.toList());
        return new Sala(entity.getId(), entity.getNombreCodigo(), pcs);
    }
}
