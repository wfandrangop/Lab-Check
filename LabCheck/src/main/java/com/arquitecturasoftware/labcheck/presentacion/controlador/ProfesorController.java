package com.arquitecturasoftware.labcheck.presentacion.controlador;

import com.arquitecturasoftware.labcheck.aplicacion.servicio.AsistenciaService;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.SesionClaseService;
import com.arquitecturasoftware.labcheck.dominio.modelo.Asignatura;
import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.AsignaturaRepository;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SalaRepository;
import com.arquitecturasoftware.labcheck.dominio.repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/profesor")
@RequiredArgsConstructor
public class ProfesorController {

    private final SesionClaseService sesionClaseService;
    private final AsistenciaService asistenciaService;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final AsignaturaRepository asignaturaRepository;


    @GetMapping("/panel")
    public String mostrarPanel(Authentication auth, Model model) {
        Usuario profesor = obtenerUsuarioAutenticado(auth);
        List<Sala> salas = salaRepository.findAll();
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        List<SesionClase> misSesiones = sesionClaseService.obtenerSesionesPorProfesor(profesor);

        model.addAttribute("profesor", profesor);
        model.addAttribute("salas", salas);
        model.addAttribute("asignaturas", asignaturas);
        model.addAttribute("misSesiones", misSesiones);
        return "profesor/panel";
    }


    @PostMapping("/activar-sesion")
    public String activarSesion(@RequestParam Long salaId,
                                @RequestParam Long asignaturaId,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Usuario profesor = obtenerUsuarioAutenticado(auth);
        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada."));
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada."));

        sesionClaseService.abrirLaboratorio(profesor, sala, asignatura);
        redirectAttributes.addFlashAttribute("mensaje", "Sesión activada exitosamente.");
        return "redirect:/profesor/panel";
    }


    @PostMapping("/cerrar-sesion")
    public String cerrarSesion(@RequestParam Long sesionId,
                               RedirectAttributes redirectAttributes) {
        sesionClaseService.cerrarSesion(sesionId);
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente.");
        return "redirect:/profesor/panel";
    }


    @GetMapping("/detalle-sesion")
    public String detalleSesion(@RequestParam Long sesionId, Model model) {
        SesionClase sesion = sesionClaseService.obtenerSesionPorId(sesionId);
        model.addAttribute("sesion", sesion);
        model.addAttribute("asistencias", asistenciaService.obtenerAsistenciasPorSesion(sesion));
        return "profesor/detalle-sesion";
    }

    private Usuario obtenerUsuarioAutenticado(Authentication auth) {
        return usuarioRepository.findByCorreoInst(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }
}
