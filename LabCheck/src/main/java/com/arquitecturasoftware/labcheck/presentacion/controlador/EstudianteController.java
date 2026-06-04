package com.arquitecturasoftware.labcheck.presentacion.controlador;

import com.arquitecturasoftware.labcheck.aplicacion.dto.AsistenciaFormDto;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.AsistenciaService;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.SesionClaseService;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class EstudianteController {

    private final SesionClaseService sesionClaseService;
    private final AsistenciaService asistenciaService;
    private final UsuarioRepository usuarioRepository;


    @GetMapping("/sesiones")
    public String mostrarSesionesActivas(Model model) {
        List<SesionClase> sesionesActivas = sesionClaseService.obtenerSesionesActivas();
        model.addAttribute("sesionesActivas", sesionesActivas);
        model.addAttribute("asistenciaForm", new AsistenciaFormDto());
        return "estudiante/sesiones";
    }


    @PostMapping("/registrar-asistencia")
    public String registrarAsistencia(@ModelAttribute AsistenciaFormDto form, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Usuario estudiante = obtenerUsuarioAutenticado(auth);
            asistenciaService.registrarAsistenciaEstudiante(estudiante, form.getSesionId(), form.getPcCodigo(), form.getNovedades());
            redirectAttributes.addFlashAttribute("mensaje", "Asistencia registrada exitosamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/estudiante/sesiones";
    }

    private Usuario obtenerUsuarioAutenticado(Authentication auth) {
        return usuarioRepository.findByCorreoInst(auth.getName()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }
}
