# LabCheck — Control y gestión de asistencias en laboratorios

LabCheck es una aplicación diseñada para gestionar el uso de laboratorios de computación: profesores abren bloques horarios para sus clases, estudiantes registran su asistencia en puestos concretos y el personal técnico documenta incidencias. El proyecto está organizado para mostrar tres aproximaciones arquitectónicas en ramas separadas del repositorio, con la intención de comparar sus implicaciones prácticas.

Ramas del repositorio

- main / metodologia-ddd: Implementación basada en Domain-Driven Design (DDD) y arquitectura hexagonal (puertos y adaptadores). Es la aproximación más cuidada en términos de modelado del dominio.
- arquitectura-en-capas: Implementación clásica por capas (Presentación, Aplicación, Dominio). Buena para proyectos con reglas moderadas y equipos acostumbrados a esta separación.
- codigo-espagueti: Prototipo monolítico y acoplado para validación rápida de flujos. Su objetivo es explorar el dominio de forma inmediata, no ser una solución a largo plazo.

Tabla de contenidos

- Descripción del proyecto
- Ramas y enfoques implementados
- Comparativa: DDD vs Arquitectura por capas vs Código espagueti
- Ventajas y desventajas por enfoque
- Conclusión y recomendación
- Nota técnica: ejecución básica

Descripción breve del proyecto

LabCheck automatiza el registro de asistencias en laboratorios, la gestión de salas y computadores, y el registro de incidencias (ReporteNovedad). Las reglas críticas del negocio —duración máxima de los bloques, ventana horaria de operación, verificación de pertenencia del computador a la sala y estados de los reportes— están implementadas en la versión DDD para garantizar coherencia y trazabilidad.

Comparativa general

| Aspecto | DDD (metodologia-ddd) | Arquitectura por capas (arquitectura-en-capas) | Código espagueti (codigo-espagueti) |
|---|---|---|---|
| Propósito | Modelar el dominio con riqueza semántica y proteger invariantes en el core. | Separar responsabilidades para facilitar mantenimiento y comprensión. | Probar ideas y flujos con la máxima velocidad.
| Organización del código | Dominio explícito, puertos/adaptadores, servicios de aplicación. | Capas claras: controladores, servicios, repositorios, modelos. | Lógica mezclada en uno o pocos archivos, muchas variables globales.
| Tiempo de entrega inicial | Mayor por modelado y diseño. | Moderado; equilibrio entre diseño y velocidad. | Muy rápido para un prototipo.
| Mantenibilidad | Alta si se mantiene disciplina. Ideal para evolución y pruebas. | Buena con disciplina; riesgo de "anemic domain" si no se cuida. | Muy baja; difícil de refactorizar.
| Testabilidad | Excelente: dominio aislado y fácil de probar. | Buena: capas facilitan mocks y pruebas unitarias. | Baja: difícil aislar componentes para tests.
| Escalabilidad del equipo | Alta: permite trabajo en paralelo y separación de responsabilidades. | Moderada: equipos pueden trabajar por capas. | Baja: cambios interferentes y conflictos frecuentes.
| Riesgo de deuda técnica | Bajo con buenas prácticas; overhead inicial. | Medio: posible acumulación de malas prácticas. | Alto: deuda crece rápidamente.
| Idoneidad para producción | Alta en sistemas con reglas complejas. | Adecuada para aplicaciones de negocio convencionales. | No recomendable para producción.

Ventajas y desventajas por enfoque

DDD (metodologia-ddd)

Ventajas:
- El dominio refleja el lenguaje del negocio, favoreciendo la comunicación entre equipo y usuarios.
- Las invariantes se concentran en los agregados, reduciendo errores de negocio.
- La arquitectura hexagonal facilita sustituir tecnologías y escribir pruebas.
- Escala bien cuando el dominio crece en complejidad.

Desventajas:
- Requiere mayor esfuerzo inicial para modelar correctamente.
- Necesita disciplina de diseño; sin ello puede convertirse en sobreingeniería.
- Más artefactos y curvas de aprendizaje para desarrolladores nuevos.

Arquitectura por capas (arquitectura-en-capas)

Ventajas:
- Estructura clara y conocida por la mayoría de desarrolladores.
- Facilita la separación de responsabilidades y la organización del equipo.
- Entregas equilibradas entre calidad y rapidez.

Desventajas:
- Riesgo de trasladar lógica de dominio a servicios, generando un modelo anémico.
- Requiere convenciones y revisiones para evitar dispersión de reglas.

Código espagueti (codigo-espagueti)

Ventajas:
- Permite validar ideas y flujos muy rápido, con la mínima inversión.
- Útil cuando se busca feedback inmediato del dominio o del usuario.

Desventajas:
- Difícil de mantener, probar y evolucionar.
- Alta propensión a errores y regresiones por acoplamiento global.
- No es una base sólida para producción.

Conclusión y recomendación

Para LabCheck, dado que el sistema gestiona reglas de negocio significativas (restricciones temporales, verificación física de computadores, trazabilidad de incidencias), la implementación basada en DDD y arquitectura hexagonal es la opción recomendada para entornos de producción y para proyectos que vayan a mantenerse y escalar en el tiempo. La rama `metodologia-ddd` recoge este enfoque con un dominio explícito, servicios de aplicación y adaptadores de infraestructura.

La arquitectura por capas es una alternativa válida cuando el dominio es relativamente estable y se busca un balance entre rapidez y orden estructural. La rama `arquitectura-en-capas` ejemplifica esta aproximación.

La rama `codigo-espagueti` es útil como artefacto de prototipo: permite validar flujos y requisitos funcionales de forma inmediata, pero debe considerarse temporal y su código no debe promoverse a producción sin una refactorización profunda.

Nota técnica: ejecución básica

Requisitos básicos: JDK 17 instalado y una base de datos PostgreSQL configurada en `src/main/resources/application.properties`. Las instrucciones completas de compilación y ejecución están en el README del proyecto; en resumen, desde la carpeta `LabCheck/\LabCheck` se usan los comandos del Maven Wrapper para compilar y ejecutar.



Este README pretende ser una guía clara y práctica para entender las decisiones arquitectónicas tomadas en cada rama y para orientar la elección del enfoque más adecuado según objetivos y contexto del proyecto.