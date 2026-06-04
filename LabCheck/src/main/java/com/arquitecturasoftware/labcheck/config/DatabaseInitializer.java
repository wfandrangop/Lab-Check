package com.arquitecturasoftware.labcheck.config;

import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando verificación y migración de contraseñas a BCrypt...");

        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            log.info("La base de datos de usuarios está vacía. Creando usuarios por defecto...");

            Usuario admin = Usuario.builder()
                    .correoInst("admin@uce.edu.ec")
                    .contrasenia(passwordEncoder.encode("admin"))
                    .nombres("Administrador")
                    .apellidos("LabCheck")
                    .cedula("1700000001")
                    .role("ROLE_ADMIN")
                    .build();

            Usuario profesor = Usuario.builder()
                    .correoInst("profesor@uce.edu.ec")
                    .contrasenia(passwordEncoder.encode("profesor"))
                    .nombres("Profesor")
                    .apellidos("LabCheck")
                    .cedula("1700000002")
                    .role("ROLE_PROFESOR")
                    .build();

            Usuario estudiante = Usuario.builder()
                    .correoInst("estudiante@uce.edu.ec")
                    .contrasenia(passwordEncoder.encode("estudiante"))
                    .nombres("Estudiante")
                    .apellidos("LabCheck")
                    .cedula("1700000003")
                    .role("ROLE_ESTUDIANTE")
                    .build();

            usuarioRepository.save(admin);
            usuarioRepository.save(profesor);
            usuarioRepository.save(estudiante);
            log.info("Usuarios por defecto creados exitosamente (admin@uce.edu.ec / admin, profesor@uce.edu.ec / profesor, estudiante@uce.edu.ec / estudiante).");
        } else {
            int actualizados = 0;
            for (Usuario usuario : usuarios) {
                String pass = usuario.getContrasenia();
                
                boolean esBcrypt = pass != null && pass.length() == 60 &&
                        (pass.startsWith("$2a$") || pass.startsWith("$2b$") || pass.startsWith("$2y$"));

                if (!esBcrypt) {
                    log.info("Detectada contraseña no codificada para el usuario: {}. Codificando con BCrypt...", usuario.getCorreoInst());
                    usuario.setContrasenia(passwordEncoder.encode(pass));
                    usuarioRepository.save(usuario);
                    actualizados++;
                }
            }
            if (actualizados > 0) {
                log.info("Se actualizaron {} contraseñas a BCrypt.", actualizados);
            } else {
                log.info("Todas las contraseñas existentes ya se encuentran codificadas correctamente.");
            }
        }
    }
}
