package taller.model;

import java.time.LocalDate;

/**
 * Clase utilitaria con métodos de validación reutilizables para el modelo.
 *
 * Todos los métodos son estáticos y lanzan {@link IllegalArgumentException}
 * con un mensaje descriptivo cuando el dato no cumple la regla definida.
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public final class Validaciones {

    private Validaciones() {}

    /**
     * Verifica que un campo de texto no sea nulo ni esté vacío.
     */
    public static void requerido(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
    }

    /**
     * Verifica que un objeto no sea nulo.
     */
    public static void noNulo(Object objeto, String campo) {
        if (objeto == null)
            throw new IllegalArgumentException(campo + " no puede ser nulo.");
    }

    /**
     * Verifica que un teléfono tenga solo dígitos y entre 7 y 15 caracteres.
     */
    public static void telefono(String telefono) {
        requerido(telefono, "El teléfono");
        String limpio = telefono.trim().replaceAll("[\\s\\-()]", "");
        if (!limpio.matches("\\d{7,15}"))
            throw new IllegalArgumentException(
                "El teléfono debe tener entre 7 y 15 dígitos. Recibido: " + telefono);
    }

    /**
     * Verifica que un texto tenga al menos una longitud mínima.
     */
    public static void longitudMinima(String valor, int minLen, String campo) {
        requerido(valor, campo);
        if (valor.trim().length() < minLen)
            throw new IllegalArgumentException(
                campo + " debe tener al menos " + minLen + " caracteres.");
    }

    /**
     * Verifica que un año de fabricación esté entre 1900 y el año actual.
     */
    public static void anioFabricacion(int anio) {
        int actual = LocalDate.now().getYear();
        if (anio < 1900 || anio > actual)
            throw new IllegalArgumentException(
                "El año debe estar entre 1900 y " + actual + ". Recibido: " + anio);
    }

    /**
     * Verifica que un costo no sea negativo.
     */
    public static void costo(double costo) {
        if (costo < 0)
            throw new IllegalArgumentException(
                "El costo no puede ser negativo. Recibido: " + costo);
    }
}
