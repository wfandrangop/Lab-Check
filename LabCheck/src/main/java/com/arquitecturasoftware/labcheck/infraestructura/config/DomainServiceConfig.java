package com.arquitecturasoftware.labcheck.infraestructura.config;

import com.arquitecturasoftware.labcheck.dominio.repositorio.SalaRepository;
import com.arquitecturasoftware.labcheck.dominio.servicio.VerificacionAsistenciaDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public VerificacionAsistenciaDomainService verificacionAsistenciaDomainService(SalaRepository salaRepository) {
        return new VerificacionAsistenciaDomainService(salaRepository);
    }
}
