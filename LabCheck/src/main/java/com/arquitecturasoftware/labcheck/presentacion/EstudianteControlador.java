package com.arquitecturasoftware.labcheck.presentacion;

import com.arquitecturasoftware.labcheck.aplicacion.dto.AsistenciaFormDto;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.AsistenciaAppService;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.BloqueHorarioAppService;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.arquitecturasoftware.labcheck.dominio.repositorio.ReporteNovedadRepository;

import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteControlador {

    private final AsistenciaAppService asistenciaAppService;
    private final BloqueHorarioAppService bloqueHorarioAppService;
    private final ReporteNovedadRepository novedadRepository;

    public EstudianteControlador(AsistenciaAppService asistenciaAppService,
                                 BloqueHorarioAppService bloqueHorarioAppService,
                                 ReporteNovedadRepository novedadRepository) {
        this.asistenciaAppService = asistenciaAppService;
        this.bloqueHorarioAppService = bloqueHorarioAppService;
        this.novedadRepository = novedadRepository;
    }

    @GetMapping
    public String mostrarDashboardEstudiante(Model modelo, jakarta.servlet.http.HttpSession session) {
        try {
            AsistenciaFormDto asistenciaFormDto = new AsistenciaFormDto();
            // Prefill estudianteId from session if present
            Object uid = session.getAttribute("currentUserId");
            if (uid instanceof Long) {
                asistenciaFormDto.setEstudianteId((Long) uid);
            }

            modelo.addAttribute("asistenciaFormDto", asistenciaFormDto);
            List<BloqueHorario> bloquesActivos = bloqueHorarioAppService.obtenerTodosBloques();
            modelo.addAttribute("bloquesDisponibles", bloquesActivos);
            modelo.addAttribute("novedades", novedadRepository.findAll());

            modelo.addAttribute("titulo", "Dashboard del Estudiante");
            return "estudiante";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar los bloques: " + excepcion.getMessage());
            return "estudiante";
        }
    }

    @GetMapping("/registrar-asistencia")
    public String formularioRegistroAsistencia(Model modelo, jakarta.servlet.http.HttpSession session) {
        AsistenciaFormDto asistenciaFormDto = new AsistenciaFormDto();
        Object uid = session.getAttribute("currentUserId");
        if (uid instanceof Long) {
            asistenciaFormDto.setEstudianteId((Long) uid);
        }
        modelo.addAttribute("asistenciaFormDto", asistenciaFormDto);
        modelo.addAttribute("titulo", "Registrar Asistencia");

        List<BloqueHorario> bloquesDisponibles = bloqueHorarioAppService.obtenerTodosBloques();
        modelo.addAttribute("bloquesDisponibles", bloquesDisponibles);

        return "estudiante";
    }

    @PostMapping("/registrar-asistencia")
    public String procesarRegistroAsistencia(@ModelAttribute AsistenciaFormDto asistenciaFormDto, Model modelo) {
        try {
            
            if (asistenciaFormDto.getBloqueId() == null ||
                    asistenciaFormDto.getEstudianteId() == null ||
                    asistenciaFormDto.getPcCodigo() == null) {
                modelo.addAttribute("error", "Todos los campos son requeridos");
                return "estudiante";
            }

            Long asistenciaId = System.currentTimeMillis();
            asistenciaAppService.registrarAsistencia(
                    asistenciaFormDto.getBloqueId(),
                    asistenciaId,
                    asistenciaFormDto.getEstudianteId(),
                    asistenciaFormDto.getPcCodigo(),
                    asistenciaFormDto.getObservaciones()
            );

            modelo.addAttribute("exito", "Asistencia registrada correctamente");
            return "redirect:/estudiante";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al registrar asistencia: " + excepcion.getMessage());
            return "estudiante";
        }
    }

    @GetMapping("/mis-asistencias")
    public String verMisAsistencias(@RequestParam(required = false) Long estudianteId, Model modelo) {
        try {
            List<BloqueHorario> bloques = bloqueHorarioAppService.obtenerTodosBloques();

            modelo.addAttribute("bloques", bloques);
            modelo.addAttribute("estudianteId", estudianteId);
            modelo.addAttribute("titulo", "Mis Asistencias");
            return "estudiante";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar asistencias: " + excepcion.getMessage());
            return "estudiante";
        }
    }

    @GetMapping("/novedades")
    public String verNovedadesReportadas(Model modelo) {
        try {
            modelo.addAttribute("titulo", "Novedades Reportadas");
            return "estudiante";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar novedades: " + excepcion.getMessage());
            return "estudiante";
        }
    }

    @PostMapping("/reportar-novedad")
    public String reportarNovedad(
            @RequestParam String pcCodigo,
            @RequestParam String descripcion,
            Model modelo) {
        try {
            if (pcCodigo == null || pcCodigo.isEmpty() || descripcion == null || descripcion.isEmpty()) {
                modelo.addAttribute("error", "El código del computador y descripción son requeridos");
                return "estudiante";
            }

            modelo.addAttribute("exito", "Novedad reportada correctamente");
            return "redirect:/estudiante";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al reportar novedad: " + excepcion.getMessage());
            return "estudiante";
        }
    }
}
