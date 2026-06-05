package com.arquitecturasoftware.labcheck.dominio.repositorio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.usuario.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository {
    void save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    Optional<Usuario> findByCedula(String cedula);

    List<Usuario> findAll();
}
