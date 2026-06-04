package com.arquitecturasoftware.labcheck.presentacion.controlador;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }


    @GetMapping("/home")
    public String redirigirPorRol(Authentication authentication) {
        if (authentication.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_PROFESOR"))) {
            return "redirect:/profesor/panel";
        } else if (authentication.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))) {
            return "redirect:/estudiante/sesiones";
        }
        return "redirect:/login";
    }
}
