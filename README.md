# Sistema de Gestión de Asistencia e Infraestructura de Laboratorios (Lab-Link UCE)

## Descripción General del Proyecto
Lab-Link UCE es un prototipo funcional de software orientado a gestionar la asistencia de estudiantes y profesores, así como el control de novedades de infraestructura en los laboratorios de computación de la Universidad Central del Ecuador (UCE). El proyecto está concebido como un prototipo para validar flujos de usuario y capturar requerimientos de forma expedita.

## Enfoque Arquitectónico: Prototipo de Código Espagueti (Monolito Acoplado)
Para maximizar la velocidad de desarrollo y validación en esta fase inicial, el sistema ha sido construido bajo un enfoque de "Código Espagueti". Este paradigma de desarrollo descarta principios de diseño limpios, patrones arquitectónicos y separación de responsabilidades en favor de un flujo de ejecución estrictamente lineal.

Las directrices fundamentales bajo las cuales se rige este código son:

1. **Estructura de Archivo Único**: El 100% de la base de código del sistema (lógica de presentación, lógica de negocio, almacenamiento en memoria y validacion de identidad) está contenida dentro del archivo principal `Main.java`, dentro de un único método `main` y careciendo de declaraciones de paquetes (`package`).
2. **Inexistencia de Capas Operativas**: No existen capas de presentación (vistas), aplicación (controladores/servicios) ni dominio (modelos/entidades). Las sentencias encargadas de imprimir texto en consola (`System.out.println`), capturar entradas (`Scanner.nextLine()`), realizar evaluaciones lógicas (`if/else`) y modificar el almacenamiento en memoria, coexisten acopladas en los mismos bloques de código.
3. **Estado Global Compartido (Memoria RAM)**: Se prescinde de bases de datos persistentes y de programación orientada a objetos (no existen clases instanciables para Profesor, Estudiante o Sala). La persistencia se simula en tiempo de ejecución utilizando variables estáticas globales (estructuras dinámicas de Java como `List` y `Map`), tratando todos los valores almacenados como cadenas de texto planas (`String`).
4. **Duplicación Explícita de Lógica**: Para mantener el paradigma de acoplamiento directo, lógicas transversales como la autenticación de usuarios (validación del dominio de correo `@uce.edu.ec` y contraseñas) no se han abstraído en métodos auxiliares ni utilitarios. Dicha validación se escribe redundante e independientemente en cada bloque del flujo correspondiente a los actores del sistema.

## Arquitectura de Datos Globales
El almacenamiento centralizado se rige por las siguientes estructuras estáticas inicializadas en la clase principal:

```java
// Colección lineal de registros de auditoría para docentes
static List<Map<String, String>> REGISTROS_PROFESORES = new ArrayList<>();

// Colección lineal de registros de asistencia y novedades de alumnos
static List<Map<String, String>> REGISTROS_ESTUDIANTES = new ArrayList<>();

// Diccionario de estado en tiempo real para laboratorios activos
static Map<String, Map<String, String>> SALA_HORARIO_ACTIVA = new HashMap<>();
```

## Secuencia del Flujo del Programa

El sistema se rige por un bucle infinito (`while`) que expone un menú principal en consola. El acceso y comportamiento de los módulos depende estrictamente de las entradas secuenciales de los actores:

### 1. Flujo del Profesor (Apertura de Sesión)
*   **Captura de Datos**: Nombres, sala ocupada, horarios y credenciales.
*   **Mecanismo de Firma**: Se valida de forma estricta (inline) que el correo posea el dominio institucional (`@uce.edu.ec`) y que la contraseña no esté vacía.
*   **Amarre de Sala**: En caso de éxito, se muta la variable `SALA_HORARIO_ACTIVA`, registrando la sala con estado `ABIERTO` y asignando al profesor responsable. Posteriormente, se anexa el historial a `REGISTROS_PROFESORES`.

### 2. Flujo del Estudiante (Registro y Amarre Funcional)
*   **Captura de Datos**: Identidad (con validación lineal de longitud de cédula de 10 dígitos), hardware asignado, novedades técnicas de la máquina, sala actual y credenciales.
*   **Mecanismo de Firma**: Validación redundante del dominio de correo y contraseña no vacía.
*   **Candado Operativo**: El registro de asistencia solo procede si la sala declarada por el estudiante existe en el mapa `SALA_HORARIO_ACTIVA` y su estado interno corresponde a `ABIERTO`. Los registros aprobados entran a `REGISTROS_ESTUDIANTES` con una firma de encargado en estado `PENDIENTE`.

### 3. Flujo del Encargado (Auditoría y Cierre)
*   **Mecanismo de Firma**: Requiere la validación inline de credenciales del administrador.
*   **Cruce de Datos (Consolidación)**: Mediante bucles anidados (`for`), el script itera sobre las salas activas y filtra la colección de estudiantes para imprimir secuencialmente (en la misma línea) los datos consolidados del estado actual, la asistencia y las novedades de hardware reportadas por los alumnos.
*   **Firma de Cierre**: El encargado tiene la potestad de finalizar la sesión. Esta acción itera y muta directamente las colecciones en memoria, cambiando el estado de los alumnos a `VALIDADO` y marcando las salas como en estado `CERRADO`.

## Instrucciones de Ejecución

Debido a su naturaleza sin dependencias externas y estructura de un solo archivo, la compilación y ejecución son directas utilizando el kit de desarrollo estándar de Java (JDK).

### Prerrequisitos
*   Java Development Kit (JDK) versión 8 o superior instalado en el entorno.

### Compilación y Ejecución por Terminal
1. Abrir la terminal o línea de comandos y navegar hasta el directorio raíz del código fuente (`src`).
2. Compilar el archivo principal:
   ```bash
   javac Main.java
   ```
3. Ejecutar la clase compilada:
   ```bash
   java Main
   ```

*Nota: Alternativamente, puede abrir la carpeta del proyecto en entornos de desarrollo integrados (IDE) como IntelliJ IDEA, Eclipse o VS Code, localizar el archivo `Main.java`, e iniciar la ejecución utilizando el botón "Run", el cual gestionará el proceso de construcción automáticamente.*
