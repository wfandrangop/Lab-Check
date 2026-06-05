package com.arquitecturasoftware.labcheck.dominio.modelo.objetoValor;

public record Cedula(String value) {
    public Cedula {
        if (value == null || value.length() != 10 || !value.matches("\\d+")) {
            throw new IllegalArgumentException("La cédula debe contener exactamente 10 dígitos numéricos.");
        }
        if (!validarCedulaEcuatoriana(value)) {
            throw new IllegalArgumentException("La cédula ingresada no es válida según el algoritmo ecuatoriano.");
        }
    }

    private static boolean validarCedulaEcuatoriana(String cedula) {
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false;
        }
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito < 0 || tercerDigito > 6) {
            return false;
        }
        int suma = 0;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int producto = digito * coeficientes[i];
            if (producto >= 10) {
                producto -= 9;
            }
            suma += producto;
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma + 9) / 10) * 10;
        int residuo = decenaSuperior - suma;
        return residuo == digitoVerificador || (residuo == 10 && digitoVerificador == 0);
    }
}
