package com.arquitecturasoftware.labcheck.presentacion;

import com.arquitecturasoftware.labcheck.aplicacion.servicio.BloqueHorarioAppService;
import com.arquitecturasoftware.labcheck.dominio.modelo.agregado.bloque.BloqueHorario;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.RangoTiempo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/profesor")
public class ProfesorControlador {

    private final BloqueHorarioAppService bloqueHorarioAppService;

    public ProfesorControlador(BloqueHorarioAppService bloqueHorarioAppService) {
        this.bloqueHorarioAppService = bloqueHorarioAppService;
    }

    @GetMapping
    public String mostrarDashboardProfesor(Model modelo) {
        try {
            List<BloqueHorario> bloquesHorarios = bloqueHorarioAppService.obtenerTodosBloques();
            modelo.addAttribute("bloquesHorarios", bloquesHorarios);
            modelo.addAttribute("titulo", "Dashboard del Profesor");
            return "profesor";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar los bloques horarios: " + excepcion.getMessage());
            return "profesor";
        }
    }

    @GetMapping("/nuevo-bloque")
    public String formularioNuevoBloque(Model modelo, jakarta.servlet.http.HttpSession session) {
        modelo.addAttribute("titulo", "Abrir Nuevo Bloque Horario");
        Object uid = session.getAttribute("currentUserId");
        if (uid != null) {
            modelo.addAttribute("currentUserId", uid);
        }
        return "profesor";
    }

    @PostMapping("/abrir-bloque")
    public String abrirBloque(
            @RequestParam(required = false) Long bloqueId,
            @RequestParam Long salaId,
            @RequestParam Long profesorId,
            @RequestParam Long asignaturaId,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            Model modelo) {
        try {
            if (horaInicio == null || horaInicio.isBlank() || horaFin == null || horaFin.isBlank()) {
                modelo.addAttribute("error", "Las horas de inicio y fin son requeridas");
                return "profesor";
            }

            LocalDateTime inicio;
            LocalDateTime fin;
            try {
                inicio = LocalDateTime.parse(horaInicio);
                fin = LocalDateTime.parse(horaFin);
            } catch (DateTimeParseException e) {
                // Intentar soportar input tipo datetime-local (sin segundos), p.e. 2026-06-04T09:00
                try {
                    if (horaInicio.length() == 16) {
                        inicio = LocalDateTime.parse(horaInicio + ":00");
                    } else {
                        inicio = LocalDateTime.parse(horaInicio);
                    }
                    if (horaFin.length() == 16) {
                        fin = LocalDateTime.parse(horaFin + ":00");
                    } else {
                        fin = LocalDateTime.parse(horaFin);
                    }
                } catch (DateTimeParseException ex2) {
                    modelo.addAttribute("error", "Formato de fecha/hora inválido. Use ISO_LOCAL_DATE_TIME, e.g. 2026-06-04T09:00:00 or use el selector de fecha/hora");
                    return "profesor";
                }
            }

            RangoTiempo rango = new RangoTiempo(inicio, fin);

            Long idBloque = bloqueId != null ? bloqueId : System.currentTimeMillis();
            bloqueHorarioAppService.abrirBloque(idBloque, salaId, profesorId, asignaturaId, rango);

            modelo.addAttribute("exito", "Bloque horario abierto correctamente");
            return "redirect:/profesor";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al abrir el bloque: " + excepcion.getMessage());
            return "profesor";
        }
    }

    @PostMapping("/cerrar-bloque/{bloqueId}")
    public String cerrarBloque(@PathVariable Long bloqueId, Model modelo) {
        try {
            bloqueHorarioAppService.cerrarBloque(bloqueId);
            modelo.addAttribute("exito", "Bloque horario cerrado correctamente");
            return "redirect:/profesor";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cerrar el bloque: " + excepcion.getMessage());
            return "profesor";
        }
    }

    @GetMapping("/bloque/{bloqueId}/asistencias")
    public String verAsistenciasDelBloque(@PathVariable Long bloqueId, Model modelo) {
        try {
            BloqueHorario bloque = bloqueHorarioAppService.obtenerTodosBloques()
                    .stream()
                    .filter(b -> b.getId().equals(bloqueId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Bloque no encontrado"));

            modelo.addAttribute("bloque", bloque);
            modelo.addAttribute("asistencias", bloque.getAsistencias());
            modelo.addAttribute("titulo", "Asistencias del Bloque");
            return "profesor";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al obtener asistencias: " + excepcion.getMessage());
            return "profesor";
        }
    }

    @PostMapping("/habilitar-asistencia/{bloqueId}")
    public String habilitarAsistencia(@PathVariable Long bloqueId, Model modelo) {
        try {
            // Invoca la orquestación que muta el estado a EstadoBloque.ACTIVO y despacha los eventos
            bloqueHorarioAppService.habilitarAsistenciaDeBloque(bloqueId);
            modelo.addAttribute("exito", "La asistencia ha sido habilitada explícitamente para los estudiantes.");
            return "redirect:/profesor";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al activar el bloque: " + excepcion.getMessage());
            return "profesor";
        }
    }
}
