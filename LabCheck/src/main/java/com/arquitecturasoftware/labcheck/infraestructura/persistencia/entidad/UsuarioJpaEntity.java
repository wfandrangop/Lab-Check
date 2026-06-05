package com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioJpaEntity {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String cedula;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "correo_institucional", nullable = false, unique = true)
    private String correoInstitucional;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String contrasena;

    public UsuarioJpaEntity() {
    }

    public UsuarioJpaEntity(Long id, String cedula, String nombre, String correoInstitucional, String rol, String contrasena) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
