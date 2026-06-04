package com.arquitecturasoftware.labcheck.presentacion.controlador;

import com.arquitecturasoftware.labcheck.aplicacion.dto.UsuarioRegistroDto;
import com.arquitecturasoftware.labcheck.aplicacion.servicio.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistroController {

    private final UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDto());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioRegistroDto registroDto, Model model) {
        try {
            usuarioService.registrarUsuario(registroDto);
            return "redirect:/login?registrado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", registroDto);
            return "registro";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado al registrar el usuario: " + e.getMessage());
            model.addAttribute("usuario", registroDto);
            return "registro";
        }
    }
}
