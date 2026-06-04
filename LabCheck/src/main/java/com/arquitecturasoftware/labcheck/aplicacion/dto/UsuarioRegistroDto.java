package com.arquitecturasoftware.labcheck.aplicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegistroDto {
    private String correoInst;
    private String contrasenia;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String role;
}
