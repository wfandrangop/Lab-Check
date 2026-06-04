package com.arquitecturasoftware.labcheck.dominio.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Pivote del horario: representa un bloque de 2 horas de clase en un laboratorio.
 * El profesor activa la sesión para habilitar el registro de asistencia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sesiones_clase")
public class SesionClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Usuario profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFin;

    @Column(nullable = false)
    private boolean activa;
}
