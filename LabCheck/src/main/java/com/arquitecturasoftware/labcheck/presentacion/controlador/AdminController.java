package com.arquitecturasoftware.labcheck.presentacion.controlador;

import com.arquitecturasoftware.labcheck.aplicacion.servicio.AsistenciaService;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.SesionClaseService;
import com.arquitecturasoftware.labcheck.dominio.modelo.Asistencia;
import com.arquitecturasoftware.labcheck.dominio.modelo.Inconveniente;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SesionClaseService sesionClaseService;
    private final AsistenciaService asistenciaService;


    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        List<SesionClase> sesiones = sesionClaseService.obtenerTodasLasSesiones();
        List<Asistencia> asistencias = asistenciaService.obtenerTodasLasAsistencias();
        List<Inconveniente> inconvenientes = asistenciaService.obtenerTodosLosInconvenientes();

        model.addAttribute("sesiones", sesiones);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("inconvenientes", inconvenientes);
        return "admin/dashboard";
    }
}
