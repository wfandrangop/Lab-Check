package com.arquitecturasoftware.labcheck.presentacion;

import com.arquitecturasoftware.labcheck.aplicacion.servicio.RegistroComputadoresAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personalLaboratorio")
public class AdminControlador {
    private final RegistroComputadoresAppService registroComputadoresAppService;

    public AdminControlador(RegistroComputadoresAppService registroComputadoresAppService) {
        this.registroComputadoresAppService = registroComputadoresAppService;
    }

    @GetMapping
    public String mostrarDashboardAdmin(Model modelo) {
        try {
            modelo.addAttribute("titulo", "Dashboard del Personal de Laboratorio");
            return "administrador";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar el dashboard: " + excepcion.getMessage());
            return "administrador";
        }
    }

    @GetMapping("/inventario-computadores")
    public String verInventarioComputadores(Model modelo) {
        try {
            modelo.addAttribute("titulo", "Inventario de Computadores");
            return "administrador";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar inventario: " + excepcion.getMessage());
            return "administrador";
        }
    }

    @PostMapping("/registrar-computador")
    public String registrarComputador(@RequestParam Long salaId, @RequestParam String codigoUnico, Model modelo) {
        try {
            if (salaId == null || codigoUnico == null || codigoUnico.isEmpty()) {
                modelo.addAttribute("error", "La sala y el código del computador son requeridos");
                return "administrador";
            }
            registroComputadoresAppService.registrarComputadorEnSala(salaId, codigoUnico);

            modelo.addAttribute("exito", "Computador registrado correctamente");
            return "redirect:/personalLaboratorio";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al registrar computador: " + excepcion.getMessage());
            return "administrador";
        }
    }

    @GetMapping("/novedades-tecnicas")
    public String verNovedadesTecnicas(Model modelo) {
        try {
            modelo.addAttribute("titulo", "Novedades Técnicas");
            return "administrador";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar novedades: " + excepcion.getMessage());
            return "administrador";
        }
    }

    @PostMapping("/actualizar-estado-novedad/{novedadId}")
    public String actualizarEstadoNovedad(@PathVariable Long novedadId, @RequestParam String nuevoEstado, Model modelo) {
        try {
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                modelo.addAttribute("error", "El nuevo estado es requerido");
                return "administrador";
            }

            modelo.addAttribute("exito", "Estado de novedad actualizado correctamente");
            return "redirect:/personalLaboratorio/novedades-tecnicas";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al actualizar novedad: " + excepcion.getMessage());
            return "administrador";
        }
    }

    @GetMapping("/auditorias")
    public String verAuditorias(Model modelo) {
        try {
            modelo.addAttribute("titulo", "Auditoría de Acciones");
            return "administrador";
        } catch (Exception excepcion) {
            modelo.addAttribute("error", "Error al cargar auditoría: " + excepcion.getMessage());
            return "administrador";
        }
    }
}
