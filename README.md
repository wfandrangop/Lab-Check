# LabCheck — Control y gestión de asistencias en laboratorios

LabCheck es una aplicación web pensada para gestionar el uso de los laboratorios de computación: los profesores abren bloques horarios para sus clases, los estudiantes registran su asistencia en puestos concretos y el personal técnico documenta incidencias. El diseño aplica Domain-Driven Design (DDD) y la Arquitectura Hexagonal para mantener la lógica del dominio separada de la infraestructura.

## Qué contiene este repositorio
- Implementación Java organizada por capas (presentación, aplicación, dominio e infraestructura).
- Plantillas Thymeleaf para la interfaz web.
- Migración Flyway para la normalización de la columna `cedula` en la tabla `usuarios`.

## Lenguaje ubicuo (resumen)

| Término | Descripción |
|---|---|
| PersonalLaboratorio | Actor administrativo que audita sesiones y atiende incidencias. |
| Profesor | Abre o cierra un BloqueHorario y supervisa la sala. |
| Estudiante | Registra su presencia en un Computador dentro de un BloqueHorario activo. |
| BloqueHorario | Agregado que representa una ventana temporal (máx. 2 horas, 08:00–20:00). |
| Asistencia | Registro que vincula Estudiante, BloqueHorario y Computador. |
| Computador | Activo físico identificado por `pcCodigo` dentro de una Sala. |
| Sala | Agregado que agrupa Computadores y garantiza unicidad de `pcCodigo`. |
| ReporteNovedad | Registro de incidencia técnica con estados: PENDIENTE → EN_REPARACION → SOLUCIONADO. |
| Usuario | Cuenta del sistema con Cedula, CorreoInstitucional y Rol. |

## Diseño táctico (componentes clave)

- Agregados: BloqueHorario, Sala, ReporteNovedad, Usuario.
- Objetos de valor: Cedula, CorreoInstitucional, RangoTiempo, Rol, EstadoBloque, EstadoNovedad.
- Eventos de dominio: AsistenciaRegistrada, BloqueHorarioActivado, BloqueHorarioCerrado, ComputadorRegistrado, AsistenciaHabilitada.
- Servicio de dominio: VerificacionAsistenciaDomainService (verifica la pertenencia del `pcCodigo` a la Sala del BloqueHorario).
- Servicios de aplicación: AsistenciaAppService, BloqueHorarioAppService, RegistroComputadoresAppService, RegistroUsuarioAppService.
- Persistencia: interfaces de repositorio en `dominio.repositorio` y adaptadores JPA en `infraestructura.persistencia.adaptador`.

## Estructura del proyecto (breve)

Raíz Java: `com.arquitecturasoftware.labcheck`

- aplicacion: DTOs y servicios de aplicación
- dominio: agregados, VOs, eventos e interfaces de repositorio
- infraestructura: entidades JPA, repositorios Spring Data y adaptadores
- presentacion: controladores MVC y plantillas Thymeleaf

Plantillas: `src/main/resources/templates` (login, registro, profesor, estudiante, administrador)

## Flujo de uso (resumen práctico)

Registro de usuario
- El formulario POST `/procesarRegistro` envía los datos.
- `RegistroUsuarioAppService` valida VOs, comprueba unicidad y persiste el `Usuario`.
- El controlador crea la sesión HTTP y redirige al dashboard según el rol.

Registro de asistencia
- El estudiante envía el formulario a `AsistenciaAppService`.
- El servicio carga el BloqueHorario y usa `VerificacionAsistenciaDomainService` para confirmar el `pcCodigo`.
- Si todo es válido, el agregado registra la Asistencia, se persiste y se publican los eventos de dominio.

## Migración de datos: cédula

Hay una migración Flyway en `src/main/resources/db/migration/V1__ensure_cedula_not_null.sql` que añade la columna `cedula` si falta, rellena valores nulos con un placeholder derivado del `id` y aplica NOT NULL + índice único. Revisar y adaptar ese script antes de aplicarlo en entornos con datos reales.

## Ejecutar la aplicación

Requisitos:
- JDK 17 (configurar `JAVA_HOME`).
- PostgreSQL configurado en `src/main/resources/application.properties`.

Desde la carpeta del proyecto (`LabCheck\\LabCheck`):

Windows:
```
.\\mvnw.cmd -DskipTests package
.\\mvnw.cmd spring-boot:run
```
Unix/macOS:
```
./mvnw -DskipTests package
./mvnw spring-boot:run
```

## Endpoints relevantes

- GET  /registro            → formulario de registro
- POST /procesarRegistro    → procesa registro de usuario
- GET  /login               → formulario de autenticación
- POST /autenticar         → procesa autenticación (flujo demo)
- GET  /profesor            → dashboard profesor
- GET  /estudiante          → dashboard estudiante
- GET  /personalLaboratorio → dashboard personal de laboratorio

Ejemplo de registro (curl):

```
curl -X POST http://localhost:8080/procesarRegistro \\
  -H "Content-Type: application/x-www-form-urlencoded" \\
  -d "nombre=Pedro%20Perez&cedula=0102030405&correoInstitucional=pedro@uce.edu.ec&rol=PROFESOR&contrasena=profesor123&confirmarContrasena=profesor123"
```

## Pruebas

Windows:
```
.\\mvnw.cmd test
```
Unix/macOS:
```
./mvnw test
```

## Notas finales

- La autenticación incluida es de demostración y no debe usarse en producción sin un sistema de seguridad adecuado.
- Antes de aplicar la migración de `cedula`, hacer copia de seguridad de la base de datos y revisar el script.

## Contacto

Historial y autores registrados en el control de versiones del repositorio.