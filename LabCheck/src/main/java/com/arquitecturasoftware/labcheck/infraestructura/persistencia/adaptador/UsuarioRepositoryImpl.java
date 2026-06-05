package com.arquitecturasoftware.labcheck.infraestructura.persistencia.adaptador;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.usuario.Usuario;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Cedula;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Rol;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad.UsuarioJpaEntity;
import com.arquitecturasoftware.labcheck.infraestructura.persistencia.repositorio.SpringDataUsuarioRepositorio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final SpringDataUsuarioRepositorio springDataUsuarioRepositorio;

    public UsuarioRepositoryImpl(SpringDataUsuarioRepositorio springDataUsuarioRepositorio) {
        this.springDataUsuarioRepositorio = springDataUsuarioRepositorio;
    }

    @Override
    public void save(Usuario usuario) {
        UsuarioJpaEntity entidad = new UsuarioJpaEntity(
                usuario.getId(),
                usuario.getCedula().value(),
                usuario.getNombre(),
                usuario.getCorreoInstitucional(),
                usuario.getRol().toString(),
                usuario.getContrasena()
        );
        springDataUsuarioRepositorio.save(entidad);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return springDataUsuarioRepositorio.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreoInstitucional(String correoInstitucional) {
        return springDataUsuarioRepositorio.findByCorreoInstitucional(correoInstitucional).map(this::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return springDataUsuarioRepositorio.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> findByCedula(String cedula) {
        return springDataUsuarioRepositorio.findByCedula(cedula).map(this::toDomain);
    }

    private Usuario toDomain(UsuarioJpaEntity entidad) {
        return new Usuario(
                entidad.getId(),
                new Cedula(entidad.getCedula()),
                entidad.getNombre(),
                entidad.getCorreoInstitucional(),
                Rol.valueOf(entidad.getRol()),
                entidad.getContrasena()
        );
    }
}
