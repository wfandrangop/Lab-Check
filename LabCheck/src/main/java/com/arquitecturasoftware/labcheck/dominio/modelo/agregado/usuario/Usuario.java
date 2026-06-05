package com.arquitecturasoftware.labcheck.dominio.modelo.agregado.usuario;

import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Rol;
import com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor.Cedula;

public class Usuario {
    private final Long id;
    private final Cedula cedula;
    private final String nombre;
    private final String correoInstitucional;
    private final Rol rol;
    private final String contrasena;

    public Usuario(Long id, Cedula cedula, String nombre, String correoInstitucional, Rol rol, String contrasena) {
        if (cedula == null) {
            throw new IllegalArgumentException("La cédula es obligatoria.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede ser nulo o vacío.");
        }
        if (correoInstitucional == null || correoInstitucional.isBlank()) {
            throw new IllegalArgumentException("El correo institucional no puede ser nulo o vacío.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo.");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }

        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.correoInstitucional = correoInstitucional;
        this.rol = rol;
        this.contrasena = contrasena;
    }

    public Long getId() {
        return id;
    }

    public Cedula getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public Rol getRol() {
        return rol;
    }

    public String getContrasena() {
        return contrasena;
    }
}
