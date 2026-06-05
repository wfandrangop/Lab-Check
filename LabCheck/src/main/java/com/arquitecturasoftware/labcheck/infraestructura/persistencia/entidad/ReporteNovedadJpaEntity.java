package com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "reportes_novedad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteNovedadJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pc_codigo", nullable = false)
    private String pcCodigo;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDate fechaReporte;

    @Column(nullable = false)
    private String estado;
}
