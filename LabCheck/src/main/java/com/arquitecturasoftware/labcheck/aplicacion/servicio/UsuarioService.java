package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.aplicacion.dto.UsuarioRegistroDto;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrarUsuario(UsuarioRegistroDto registroDto) {
        if (usuarioRepository.existsByCorreoInst(registroDto.getCorreoInst())) {
            throw new IllegalArgumentException("El correo institucional ya está registrado.");
        }

        // Asegurar que el rol tenga el prefijo ROLE_ requerido por Spring Security
        String roleName = registroDto.getRole().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // Validar que el rol sea uno de los permitidos
        if (!roleName.equals("ROLE_ESTUDIANTE") && !roleName.equals("ROLE_PROFESOR") && !roleName.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Rol inválido especificado.");
        }

        Usuario usuario = Usuario.builder()
                .correoInst(registroDto.getCorreoInst())
                .contrasenia(passwordEncoder.encode(registroDto.getContrasenia()))
                .nombres(registroDto.getNombres())
                .apellidos(registroDto.getApellidos())
                .cedula(registroDto.getCedula())
                .role(roleName)
                .build();

        return usuarioRepository.save(usuario);
    }
}
