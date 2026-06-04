# LabCheck — Sistema de Gestión de Asistencia en Laboratorios UCE

Software de gestión de asistencia, control de equipos y novedades en los laboratorios de la Universidad Central del Ecuador (UCE). Este proyecto fue diseñado y estructurado bajo una **Arquitectura en Capas** con el fin de implementar un sistema desacoplado, robusto y fácil de mantener.

---

## Tabla de Contenidos

1. [Descripción del Proyecto](#1-descripción-del-proyecto)
2. [Arquitectura del Software](#2-arquitectura-del-software)
   - [Estructura del Proyecto](#estructura-del-proyecto)
   - [Flujo de Dependencias](#flujo-de-dependencias)
3. [Tecnologías y Librerías](#3-tecnologías-y-librerías)
4. [Requisitos Previos y Configuración](#4-requisitos-previos-y-configuración)
5. [Población Obligatoria de Datos Semilla (SQL)](#5-población-obligatoria-de-datos-semilla-sql)
6. [Instrucciones de Ejecución](#6-instrucciones-de-ejecución)
7. [Credenciales por Defecto](#7-credenciales-por-defecto)
8. [Limitaciones del Sistema](#8-limitaciones-del-sistema)

---

## 1. Descripción del Proyecto

**LabCheck** es una solución web para registrar la asistencia de los estudiantes en los laboratorios de computación de la universidad. Permite a los profesores activar y reservar sesiones de clase en salas específicas, y a los estudiantes ingresar su asistencia asociándola a una computadora física del laboratorio. Además, los estudiantes pueden reportar cualquier novedad técnica (daños, fallos de software o hardware) que se asocia directamente a la máquina utilizada, facilitando la auditoría técnica por parte del administrador del sistema.

Este software implementa restricciones lógicas complejas de negocio, tales como:

- **Exclusividad de reserva**: No se permite que dos profesores reserven o activen sesiones de clase en la misma sala y en horarios que se solapen.
- **Asistencia única**: Un estudiante solo puede registrar su asistencia en una única sesión de clase activa a la vez. Una vez registrado, el sistema bloquea visual y lógicamente el registro en cualquier otra sesión activa paralela.

---

## 2. Arquitectura del Software

El sistema está desarrollado siguiendo una **Arquitectura en Capas** limpia , donde cada capa tiene responsabilidades bien definidas:

```
[ Capa de Presentación ]  --> Controladores web (MVC) y plantillas Thymeleaf.
         |
         v
[ Capa de Aplicación ]   --> Casos de uso, DTOs y Servicios de lógica de negocio.
         |
         v
[  Capa de Dominio   ]   --> Entidades del modelo de negocio e interfaces de persistencia.
```

### Estructura del Proyecto

- **Dominio (`com.arquitecturasoftware.labcheck.dominio`)**:
  - **`modelo`**: Contiene las entidades principales que representan el modelo del dominio académico y físico (`Usuario`, `Asignatura`, `Sala`, `Computador`, `SesionClase`, `Asistencia`, `Inconveniente`).
  - **`repositorio`**: Interfaces que definen los contratos para interactuar con la base de datos (heredan de `JpaRepository`).
- **Aplicación (`com.arquitecturasoftware.labcheck.aplicacion`)**:
  - **`servicio`**: Clases de servicio (`UsuarioService`, `SesionClaseService`, `AsistenciaService`) que coordinan los flujos de negocio y validaciones del sistema.
  - **`dto`**: Objetos de transferencia de datos (`UsuarioRegistroDto`, `AsistenciaFormDto`) para transportar información limpia entre el frontend y los servicios.
- **Presentación (`com.arquitecturasoftware.labcheck.presentacion`)**:
  - **`controlador`**: Controladores de Spring MVC (`AdminController`, `ProfesorController`, `EstudianteController`, `LoginController`, `RegistroController`) encargados de mapear las solicitudes HTTP y renderizar las vistas Thymeleaf correspondientes.
  - **`excepcion`**: Manejador global de excepciones del sistema.

### Flujo de Dependencias

El flujo sigue una regla estricta: las capas externas dependen de las internas, pero el Dominio no depende de ninguna otra capa ni de frameworks específicos de negocio externos. La persistencia es delegada mediante el uso de Spring Data JPA.

---

## 3. Tecnologías y Librerías

El ecosistema tecnológico utilizado en el proyecto se compone de:

- **Java 17**: Lenguaje de programación principal.
- **Spring Boot 4.0.6**: Framework para simplificar la configuración y despliegue.
  - **Spring Web**: Para la construcción de la aplicación web tipo MVC.
  - **Spring Security**: Para la gestión de autenticación, control de accesos, roles y protección contra ataques comunes.
  - **Spring Data JPA**: Abstracción para el acceso a datos mediante ORM (Hibernate).
  - **Thymeleaf**: Motor de plantillas HTML5 para renderizar el frontend de forma dinámica.
- **Lombok**: Librería para reducir el código redundante o boilerplate mediante anotaciones (como getters, setters, builders y constructores).
- **PostgreSQL**: Sistema de gestión de base de datos relacional para entornos de producción y desarrollo.

---

## 4. Requisitos Previos y Configuración

Antes de ejecutar la aplicación, asegúrese de contar con:

1. **Java Development Kit (JDK) 17** o superior instalado.
2. **PostgreSQL** corriendo en su máquina local o servidor.
3. Un gestor de base de datos (DBeaver, pgAdmin o consola `psql`).

### Configuración del archivo `application.properties`

Edite las propiedades de conexión a la base de datos ubicadas en `src/main/resources/application.properties` para adaptarlas a sus credenciales de PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lab_check_capas
spring.datasource.username=postgres
spring.datasource.password=SU_CONTRASENA_AQUI
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> [¡IMPORTANTE!]
> Cree la base de datos vacía en PostgreSQL antes de ejecutar el programa. Por ejemplo:
>
> ```sql
> CREATE DATABASE lab_check_capas;
> ```

---

## 5. Población Obligatoria de Datos Semilla (SQL)

La aplicación valida lógicamente que una sesión de clase ocurra en una sala física registrada y que el estudiante registre su asistencia en una computadora física válida asociada a dicha sala.

**Si la base de datos no tiene datos de salas, computadores y asignaturas registrados de antemano, el profesor no podrá activar laboratorios y los estudiantes no podrán registrar asistencias.**

Por favor, ejecute el siguiente script SQL en su base de datos una vez que Hibernate haya creado las tablas (es decir, después de iniciar el proyecto por primera vez o usando la consola de base de datos):

```sql
-- 1. Insertar Salas de Laboratorio
INSERT INTO salas VALUES
(1,'SALA-01' ),
(2, 'SALA-02' ),
(3, 'SALA-03' )
ON CONFLICT DO NOTHING;

-- 2. Insertar Computadoras asociadas a las Salas
-- (Se asume que SALA-01 tiene ID 1, SALA-02 tiene ID 2, etc.)
INSERT INTO computadores (codigo_unico, id, sala_id) VALUES
('PC-01',1, 1),
('PC-02',2, 1),
('PC-03',3, 1),
('PC-04',4, 1),
('PC-05',5, 1),
('PC-06',6, 2),
('PC-07',7, 2),
('PC-08',8, 2),
('PC-09',9, 3),
('PC-10',10, 3)
ON CONFLICT DO NOTHING;

-- 3. Insertar Asignaturas Académicas
INSERT INTO asignaturas (nombre) VALUES
('Arquitectura de Software'),
('Diseño y Patrones de Software'),
('Desarrollo de Aplicaciones Web'),
('Sistemas Distribuidos'),
('Administración de Redes')
ON CONFLICT DO NOTHING;
```

---

## 6. Instrucciones de Ejecución

1. Abra una terminal en el directorio raíz del proyecto (`LabCheck`), tambien lo puede hacer desde un IDE la ejecución.
2. Limpie e inicie la aplicación usando el Maven Wrapper incorporado:
   - **En Windows (PowerShell / CMD)**:
     ```bash
     .\mvnw spring-boot:run
     ```
   - **En macOS / Linux**:
     ```bash
     chmod +x mvnw
     ./mvnw spring-boot:run
     ```
3. Espere a que la consola indique que el servidor Tomcat se ha iniciado exitosamente en el puerto predeterminado:
   ```
   Tomcat started on port 8080 (http) with context path '/'
   ```
4. Abra su navegador web e ingrese a: `http://localhost:8080`

---

## 7. Credenciales por Defecto

El sistema cuenta con un inicializador dinámico (`DatabaseInitializer`). Si al arrancar la aplicación detecta que la tabla de usuarios está vacía, creará automáticamente las siguientes cuentas de prueba con contraseñas encriptadas mediante BCrypt para facilitar el acceso inmediato:

| Correo Institucional    | Contraseña   | Rol                            | Vista Asociada         |
| :---------------------- | :----------- | :----------------------------- | :--------------------- |
| `admin@uce.edu.ec`      | `admin`      | Administrador (`ROLE_ADMIN`)   | `/admin/dashboard`     |
| `profesor@uce.edu.ec`   | `profesor`   | Profesor (`ROLE_PROFESOR`)     | `/profesor/panel`      |
| `estudiante@uce.edu.ec` | `estudiante` | Estudiante (`ROLE_ESTUDIANTE`) | `/estudiante/sesiones` |

> [!TIP]
> Cualquier usuario nuevo puede registrarse en el sistema libremente utilizando el enlace "Regístrate aquí" en la pantalla de inicio de sesión (`/registro`). Las contraseñas ingresadas serán hasheadas automáticamente mediante BCrypt.

---

## 8. Limitaciones del Sistema

Al tratarse de una versión enfocada en la arquitectura limpia y lógica de asistencia central, esta entrega presenta las siguientes limitantes que deben ser tomadas en cuenta:

1. **Gestión Manual de Infraestructura**: El sistema no posee pantallas administrativas en el frontend para crear o editar nuevas salas, computadoras o asignaturas. Actualmente, este registro debe realizarse de forma directa en la base de datos (vía scripts SQL).
2. **Duración Fija de las Sesiones**: Al activar una sesión de clase, el sistema calcula automáticamente una duración fija de **2 horas** desde la hora de inicio seleccionada. No existe la posibilidad de definir duraciones personalizadas variables (por ejemplo, clases de 1 hora o talleres de 3 horas).
3. **Ausencia de Geolocalización o Red Exclusiva**: El estudiante puede registrar su asistencia desde cualquier dispositivo que tenga acceso a la URL del sistema. La validación del código de la PC es lógica (basada en que el código exista en la base de datos y pertenezca a la sala), pero no verifica la IP local del equipo ni restringe el envío a una red local exclusiva del laboratorio.
4. **Cierre Manual de Laboratorios**: Las clases activadas permanecen abiertas en la base de datos de manera indefinida a menos que el profesor ejecute manualmente la acción de "Cerrar" en su panel, o bien que haya transcurrido la hora de fin asignada lógicamente, lo que bloquea futuros registros, pero la sesión permanecerá en estado "Activo" en la lista hasta su cierre manual.
5. **Dependencia Fuerte de PostgreSQL**: La aplicación requiere una conexión permanente con PostgreSQL activa; no está implementada una base de datos embebida (como H2 en memoria) para demostraciones instantáneas _offline_ sin configuración.
