package com.arquitecturasoftware.labcheck.presentacion;

import com.arquitecturasoftware.labcheck.aplicacion.dto.RegistroUsuarioFormDto;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.RegistroUsuarioAppService;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.usuario.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class LoginControlador {

    private final RegistroUsuarioAppService registroUsuarioAppService;
    private final UsuarioRepository usuarioRepository;

    public LoginControlador(RegistroUsuarioAppService registroUsuarioAppService, UsuarioRepository usuarioRepository) {
        this.registroUsuarioAppService = registroUsuarioAppService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String mostrarLoginInicial(HttpSession session) {
        Object rol = session != null ? session.getAttribute("currentUserRol") : null;
        if (rol != null) {
            switch (rol.toString()) {
                case "PROFESOR": return "redirect:/profesor";
                case "ESTUDIANTE": return "redirect:/estudiante";
                case "PERSONAL_LABORATORIO": return "redirect:/personalLaboratorio";
            }
        }
        return "login";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model modelo, HttpSession session) {
        Object rol = session != null ? session.getAttribute("currentUserRol") : null;
        if (rol != null) {
            switch (rol.toString()) {
                case "PROFESOR": return "redirect:/profesor";
                case "ESTUDIANTE": return "redirect:/estudiante";
                case "PERSONAL_LABORATORIO": return "redirect:/personalLaboratorio";
            }
        }
        modelo.addAttribute("titulo", "Autenticación LabCheck");
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("registroUsuarioFormDto", new RegistroUsuarioFormDto());
        modelo.addAttribute("titulo", "Registro de Nuevo Usuario");
        return "registro";
    }

    @PostMapping("/procesarRegistro")
    public String procesarRegistroUsuario(@ModelAttribute RegistroUsuarioFormDto formulario, Model modelo, HttpSession session) {
        try {
            // Validaciones básicas
            if (formulario.getNombre() == null || formulario.getNombre().isBlank()) {
                modelo.addAttribute("error", "El nombre es obligatorio");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }
            if (formulario.getCedula() == null || formulario.getCedula().isBlank()) {
                modelo.addAttribute("error", "La cédula es obligatoria");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }
            if (formulario.getCorreoInstitucional() == null || formulario.getCorreoInstitucional().isBlank()) {
                modelo.addAttribute("error", "El correo institucional es obligatorio");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }
            if (formulario.getRol() == null || formulario.getRol().isBlank()) {
                modelo.addAttribute("error", "Debe seleccionar un rol");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }
            if (formulario.getContrasena() == null || formulario.getContrasena().isBlank()) {
                modelo.addAttribute("error", "La contraseña es obligatoria");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }
            if (!formulario.getContrasena().equals(formulario.getConfirmarContrasena())) {
                modelo.addAttribute("error", "Las contraseñas no coinciden");
                modelo.addAttribute("registroUsuarioFormDto", formulario);
                return "registro";
            }

            // Llamar al servicio de aplicación (incluye cédula)
            registroUsuarioAppService.registrarNuevoUsuario(
                    formulario.getNombre(),
                    formulario.getCedula(),
                    formulario.getCorreoInstitucional(),
                    formulario.getRol(),
                    formulario.getContrasena()
            );

            // Obtiene el usuario recién creado para iniciar sesión en la sesión HTTP
            Optional<Usuario> creado = usuarioRepository.findByCorreoInstitucional(formulario.getCorreoInstitucional());
            creado.ifPresent(u -> {
                session.setAttribute("currentUserId", u.getId());
                session.setAttribute("currentUserRol", u.getRol().toString());
                session.setAttribute("currentUserNombre", u.getNombre());
            });

            // Redirección según rol (prioriza rol persistido)
            String rol;
            if (creado.isPresent()) {
                rol = creado.get().getRol().toString();
            } else {
                rol = formulario.getRol() == null ? "" : formulario.getRol().toUpperCase().trim();
            }

            switch (rol) {
                case "PROFESOR":
                    return "redirect:/profesor";
                case "ESTUDIANTE":
                    return "redirect:/estudiante";
                case "PERSONAL_LABORATORIO":
                    return "redirect:/personalLaboratorio";
                default:
                    modelo.addAttribute("exito", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
                    return "login";
            }
        } catch (IllegalArgumentException excepcion) {
            modelo.addAttribute("error", excepcion.getMessage());
            modelo.addAttribute("registroUsuarioFormDto", formulario);
            return "registro";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al registrar usuario: " + excepcion.getMessage());
            modelo.addAttribute("registroUsuarioFormDto", formulario);
            return "registro";
        }
    }

    @PostMapping("/autenticar")
    public String procesarAutenticacion(String correo, String contrasenia, Model modelo, HttpSession session) {
        try {
            if (correo == null || correo.isEmpty() || contrasenia == null || contrasenia.isEmpty()) {
                modelo.addAttribute("error", "Las credenciales no pueden estar vacías");
                return "login";
            }

            // Buscar usuario y validar contraseña (sin seguridad por simplicidad)
            Optional<Usuario> opt = usuarioRepository.findByCorreoInstitucional(correo);
            if (opt.isEmpty()) {
                modelo.addAttribute("error", "Credenciales inválidas");
                return "login";
            }
            Usuario usuario = opt.get();
            if (!usuario.getContrasena().equals(contrasenia)) {
                modelo.addAttribute("error", "Credenciales inválidas");
                return "login";
            }

            // Guardar en sesión y redireccionar según rol
            session.setAttribute("currentUserId", usuario.getId());
            session.setAttribute("currentUserRol", usuario.getRol().toString());
            session.setAttribute("currentUserNombre", usuario.getNombre());

            String rol = usuario.getRol().toString();
            return switch (rol) {
                case "PROFESOR" -> "redirect:/profesor";
                case "ESTUDIANTE" -> "redirect:/estudiante";
                case "PERSONAL_LABORATORIO" -> "redirect:/personalLaboratorio";
                default -> "login";
            };
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error durante la autenticación: " + excepcion.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
