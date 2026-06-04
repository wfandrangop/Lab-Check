package com.arquitecturasoftware.labcheck.aplicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaFormDto {

    private Long sesionId;

    private String pcCodigo;

    private String novedades;
}
