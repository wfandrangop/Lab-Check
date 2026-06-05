package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.usuario.Usuario;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Cedula;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Rol;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistroUsuarioAppService {
    private final UsuarioRepository usuarioRepository;

    public RegistroUsuarioAppService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void registrarNuevoUsuario(String nombre, String cedulaValue, String correoInstitucional, String rolString, String contrasena) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (cedulaValue == null || cedulaValue.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria.");
        }
        if (correoInstitucional == null || correoInstitucional.isBlank()) {
            throw new IllegalArgumentException("El correo institucional es obligatorio.");
        }
        if (!correoInstitucional.contains("@")) {
            throw new IllegalArgumentException("El correo institucional debe ser válido.");
        }
        if (contrasena == null || contrasena.isBlank() || contrasena.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }

        // Verificar si el usuario ya existe por correo o cédula
        if (usuarioRepository.findByCorreoInstitucional(correoInstitucional).isPresent()) {
            throw new IllegalArgumentException("El correo institucional " + correoInstitucional + " ya está registrado.");
        }
        if (usuarioRepository.findByCedula(cedulaValue).isPresent()) {
            throw new IllegalArgumentException("La cédula " + cedulaValue + " ya está registrada.");
        }

        // Validar rol
        Rol rol;
        try {
            rol = Rol.valueOf(rolString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + rolString);
        }

        // Crear el agregado Usuario (Cedula valida internamente)
        Cedula cedula = new Cedula(cedulaValue);
        Long usuarioId = System.currentTimeMillis();
        Usuario nuevoUsuario = new Usuario(usuarioId, cedula, nombre, correoInstitucional, rol, contrasena);

        // Persistir
        usuarioRepository.save(nuevoUsuario);
    }

    public void verificarDisponibilidadCorreo(String correoInstitucional) {
        if (usuarioRepository.findByCorreoInstitucional(correoInstitucional).isPresent()) {
            throw new IllegalArgumentException("El correo institucional ya está en uso.");
        }
    }
}
