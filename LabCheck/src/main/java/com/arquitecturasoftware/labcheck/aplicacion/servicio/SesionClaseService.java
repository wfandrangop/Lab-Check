package com.arquitecturasoftware.labcheck.aplicacion.servicio;

import com.arquitecturasoftware.labcheck.dominio.modelo.Asignatura;
import com.arquitecturasoftware.labcheck.dominio.modelo.Sala;
import com.arquitecturasoftware.labcheck.dominio.modelo.SesionClase;
import com.arquitecturasoftware.labcheck.dominio.modelo.Usuario;
import com.arquitecturasoftware.labcheck.dominio.repositorio.SesionClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionClaseService {

    private final SesionClaseRepository sesionClaseRepository;

    @Transactional
    public SesionClase abrirLaboratorio(Usuario profesor, Sala sala, Asignatura asignatura, LocalDateTime horaInicio) {
        LocalDateTime horaFin = horaInicio.plusHours(2);

        List<SesionClase> solapadas = sesionClaseRepository.findOverlappingSessions(sala, horaInicio, horaFin);
        if (!solapadas.isEmpty()) {
            SesionClase conflicto = solapadas.get(0);
            Usuario otroProfesor = conflicto.getProfesor();
            throw new IllegalStateException("La sala ya está ocupada por el profesor " +
                    otroProfesor.getNombres() + " " + otroProfesor.getApellidos() +
                    " en el horario de " + conflicto.getHoraInicio().toLocalTime() + " a " + conflicto.getHoraFin().toLocalTime() + ".");
        }

        SesionClase sesion = SesionClase.builder()
                .sala(sala)
                .profesor(profesor)
                .asignatura(asignatura)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .activa(true)
                .build();
        return sesionClaseRepository.save(sesion);
    }


    @Transactional
    public void cerrarSesion(Long sesionId) {
        SesionClase sesion = sesionClaseRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + sesionId));
        sesion.setActiva(false);
        sesionClaseRepository.save(sesion);
    }


    @Transactional(readOnly = true)
    public List<SesionClase> obtenerSesionesActivas() {
        return sesionClaseRepository.findByActivaTrue();
    }


    @Transactional(readOnly = true)
    public List<SesionClase> obtenerSesionesPorProfesor(Usuario profesor) {
        return sesionClaseRepository.findByProfesor(profesor);
    }


    @Transactional(readOnly = true)
    public List<SesionClase> obtenerTodasLasSesiones() {
        return sesionClaseRepository.findAll();
    }


    @Transactional(readOnly = true)
    public SesionClase obtenerSesionPorId(Long id) {
        return sesionClaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + id));
    }
}
