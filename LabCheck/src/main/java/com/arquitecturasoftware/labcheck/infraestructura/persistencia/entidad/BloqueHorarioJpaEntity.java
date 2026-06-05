package com.arquitecturasoftware.labcheck.infraestructura.persistencia.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bloques_horarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BloqueHorarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sala_id", nullable = false)
    private Long salaId;

    @Column(name = "profesor_id", nullable = false)
    private Long profesorId;

    @Column(name = "asignatura_id", nullable = false)
    private Long asignaturaId;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalDateTime horaFin;

    @Column(nullable = false)
    private String estado;

    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AsistenciaJpaEntity> asistencias = new ArrayList<>();
}
