import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static List<Map<String, String>> REGISTROS_PROFESORES = new ArrayList<>();
    static List<Map<String, String>> REGISTROS_ESTUDIANTES = new ArrayList<>();
    static Map<String, Map<String, String>> SALA_HORARIO_ACTIVA = new HashMap<>();

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- SISTEMA DE GESTION DE ASISTENCIA E INFRAESTRUCTURA DE LABORATORIOS (SGA-Lab UCE) ---");
            System.out.println("1. Registro de Profesor (Clase)");
            System.out.println("2. Registro de Estudiante (Asistencia)");
            System.out.println("3. Modulo del Encargado (Consolidacion, Verificacion y Cierre)");
            System.out.println("4. Salir");
            System.out.println("Seleccione una opcion: ");
            String opcion = teclado.nextLine();

            if (opcion.equals("1")) {
                System.out.println("--- REGISTRO DE PROFESOR ---");
                System.out.println("Nombres del Profesor: ");
                String nombres_profesor = teclado.nextLine();
                System.out.println("Apellidos del Profesor: ");
                String apellidos_profesor = teclado.nextLine();
                System.out.println("Sala a ocupar (Ej: LAB-01): ");
                String sala_ocupada = teclado.nextLine();
                System.out.println("Hora de Ingreso (Ej: 08:00): ");
                String hora_ingreso = teclado.nextLine();
                System.out.println("Hora de Salida (Ej: 10:00): ");
                String hora_salida = teclado.nextLine();
                System.out.println("Correo Institucional: ");
                String correo_inst = teclado.nextLine();
                System.out.println("Contrasenia: ");
                String contrasenia = teclado.nextLine();


                if (correo_inst.contains("@uce.edu.ec") && !contrasenia.trim().isEmpty()) {
                    Map<String, String> infoSala = new HashMap<>();
                    infoSala.put("profesor", correo_inst);
                    infoSala.put("hora_inicio", hora_ingreso);
                    infoSala.put("estado", "ABIERTO");
                    SALA_HORARIO_ACTIVA.put(sala_ocupada, infoSala);

                    Map<String, String> regProfesor = new HashMap<>();
                    regProfesor.put("nombres_profesor", nombres_profesor);
                    regProfesor.put("apellidos_profesor", apellidos_profesor);
                    regProfesor.put("sala_ocupada", sala_ocupada);
                    regProfesor.put("hora_ingreso", hora_ingreso);
                    regProfesor.put("hora_salida", hora_salida);
                    regProfesor.put("correo_inst", correo_inst);
                    regProfesor.put("contrasenia", contrasenia);
                    REGISTROS_PROFESORES.add(regProfesor);

                    System.out.println("Exito: Sesion de clase iniciada y sala " + sala_ocupada + " activa.");
                } else {
                    System.out.println("Error: Firma invalida. El correo debe pertenecer a @uce.edu.ec y la contrasenia no puede estar vacia.");
                }

            } else if (opcion.equals("2")) {
                System.out.println("--- REGISTRO DE ESTUDIANTE ---");
                System.out.println("Nombres del Estudiante: ");
                String nombres_estudiante = teclado.nextLine();
                System.out.println("Apellidos del Estudiante: ");
                String apellidos_estudiante = teclado.nextLine();
                System.out.println("Cedula de Identidad (10 digitos): ");
                String cedula_identidad = teclado.nextLine();
                System.out.println("Codigo de Computador: ");
                String codigo_computador = teclado.nextLine();
                System.out.println("Novedades del Equipo: ");
                String novedades_equipo = teclado.nextLine();
                System.out.println("Sala de Ingreso (Ej: LAB-01): ");
                String sala_ingreso = teclado.nextLine();
                System.out.println("Correo Institucional Estudiante: ");
                String correo_inst = teclado.nextLine().trim();
                System.out.println("Contrasenia Estudiante: ");
                String contrasenia = teclado.nextLine();

                if (cedula_identidad.length() != 10) {
                    System.out.println("Error: La longitud de la cedula de identidad no es de 10 digitos.");
                } else {
                    if (correo_inst.contains("@uce.edu.ec") && !contrasenia.trim().isEmpty()) {
                        if (SALA_HORARIO_ACTIVA.containsKey(sala_ingreso) &&
                                SALA_HORARIO_ACTIVA.get(sala_ingreso).get("estado").equals("ABIERTO")) {

                            Map<String, String> regEst = new HashMap<>();
                            regEst.put("nombres_estudiante", nombres_estudiante);
                            regEst.put("apellidos_estudiante", apellidos_estudiante);
                            regEst.put("cedula_identidad", cedula_identidad);
                            regEst.put("codigo_computador", codigo_computador);
                            regEst.put("novedades_equipo", novedades_equipo);
                            regEst.put("sala", sala_ingreso);
                            regEst.put("correo_inst", correo_inst);
                            regEst.put("contrasenia", contrasenia);
                            regEst.put("firma_encargado", "PENDIENTE");
                            REGISTROS_ESTUDIANTES.add(regEst);

                            System.out.println("Exito: Estudiante registrado y vinculado a la clase de la sala " + sala_ingreso + ".");
                        } else {
                            System.out.println("Error: No existe una clase activa en esta sala o el profesor no ha firmado el ingreso.");
                        }
                    } else {
                        System.out.println("Error: Firma del estudiante invalida. El correo debe pertenecer a @uce.edu.ec y la contrasenia no puede estar vacia.");
                    }
                }

            } else if (opcion.equals("3")) {
                System.out.println("--- MODULO DEL ENCARGADO ---");
                System.out.println("Correo Institucional Encargado: ");
                String correo_inst = teclado.nextLine();
                System.out.println("Contrasenia Encargado: ");
                String contrasenia = teclado.nextLine();

                if (correo_inst.contains("@uce.edu.ec") && !contrasenia.trim().isEmpty()) {
                    System.out.println("\n--- REPORTE DE INFRAESTRUCTURA Y ASISTENCIA ---");

                    for (String salaClave : SALA_HORARIO_ACTIVA.keySet()) {
                        System.out.println("Sala: " + salaClave + " | Hora Apertura: " + SALA_HORARIO_ACTIVA.get(salaClave).get("hora_inicio") + " | Profesor Responsable: " + SALA_HORARIO_ACTIVA.get(salaClave).get("profesor"));
                        boolean flagEst = false;
                        for (Map<String, String> estudiante : REGISTROS_ESTUDIANTES) {
                            if (estudiante.get("sala").equals(salaClave)) {
                                System.out.println("  [Alumno: " + estudiante.get("nombres_estudiante") + " " + estudiante.get("apellidos_estudiante") +
                                        " | Cedula: " + estudiante.get("cedula_identidad") +
                                        " | Equipo: " + estudiante.get("codigo_computador") +
                                        " | Novedades: " + estudiante.get("novedades_equipo") +
                                        " | Estado Firma: " + estudiante.get("firma_encargado") + "]");
                                flagEst = true;
                            }
                        }
                        if (!flagEst) {
                            System.out.println("  No hay estudiantes registrados.");
                        }
                    }

                    System.out.println("\nDesea validar las firmas de asistencia y cerrar el bloque de laboratorios? (si/no): ");
                    String validar = teclado.nextLine();

                    if (validar.equalsIgnoreCase("si")) {
                        for (String salaClave : SALA_HORARIO_ACTIVA.keySet()) {
                            SALA_HORARIO_ACTIVA.get(salaClave).put("estado", "CERRADO");
                        }
                        for (Map<String, String> estudiante : REGISTROS_ESTUDIANTES) {
                            estudiante.put("firma_encargado", "VALIDADO");
                        }
                        System.out.println("Exito: Bloque de laboratorios cerrado. Registros de estudiantes mutados a VALIDADO y salas en estado CERRADO.");
                    } else {
                        System.out.println("Operacion de cierre cancelada.");
                    }
                } else {
                    System.out.println("Error: Acceso denegado. El correo debe pertenecer a @uce.edu.ec y la contrasenia no puede estar vacia.");
                }

            } else if (opcion.equals("4")) {
                System.out.println("Saliendo del sistema...");
                break;
            } else {
                System.out.println("Opcion no valida. Intente nuevamente.");
            }
        }
        teclado.close();
    }
    private static String leerEntrada(Scanner teclado, String mensaje) {
        String entrada = "";

        while (entrada.isEmpty()) {
            System.out.print(mensaje);
            entrada = teclado.nextLine().trim();
        }
        return entrada;
    }
}
