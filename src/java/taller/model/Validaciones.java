package taller.model;

import java.time.LocalDate;

/**
 * Clase utilitaria con métodos de validación reutilizables para el modelo.
 *
 * <p>Todos los métodos son estáticos y lanzan {@link IllegalArgumentException}
 * con un mensaje descriptivo cuando el dato no cumple la regla definida.
 * No se puede instanciar.</p>
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public final class Validaciones {

    private Validaciones() {}

    /**
     * Verifica que un campo de texto no sea nulo ni esté vacío.
     *
     * @param valor Valor a validar.
     * @param campo Nombre descriptivo del campo (para el mensaje de error).
     * @throws IllegalArgumentException si el valor es nulo o solo espacios.
     */
    public static void requerido(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
    }

    /**
     * Verifica que un objeto no sea nulo.
     *
     * @param objeto Objeto a verificar.
     * @param campo  Nombre descriptivo del campo.
     * @throws IllegalArgumentException si el objeto es nulo.
     */
    public static void noNulo(Object objeto, String campo) {
        if (objeto == null)
            throw new IllegalArgumentException(campo + " no puede ser nulo.");
    }

    /**
     * Verifica que un teléfono tenga solo dígitos y entre 7 y 15 caracteres.
     *
     * @param telefono Número de teléfono a validar.
     * @throws IllegalArgumentException si el formato es inválido.
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
     *
     * @param valor  Texto a validar.
     * @param minLen Longitud mínima requerida.
     * @param campo  Nombre descriptivo del campo.
     * @throws IllegalArgumentException si el texto es más corto que el mínimo.
     */
    public static void longitudMinima(String valor, int minLen, String campo) {
        requerido(valor, campo);
        if (valor.trim().length() < minLen)
            throw new IllegalArgumentException(
                campo + " debe tener al menos " + minLen + " caracteres.");
    }

    /**
     * Verifica que un año de fabricación esté entre 1900 y el año actual.
     *
     * @param anio Año a validar.
     * @throws IllegalArgumentException si el año está fuera del rango permitido.
     */
    public static void anioFabricacion(int anio) {
        int actual = LocalDate.now().getYear();
        if (anio < 1900 || anio > actual)
            throw new IllegalArgumentException(
                "El año debe estar entre 1900 y " + actual + ". Recibido: " + anio);
    }

    /**
     * Verifica que un costo no sea negativo (0 se permite, ej. garantía).
     *
     * @param costo Costo a validar.
     * @throws IllegalArgumentException si el costo es negativo.
     */
    public static void costo(double costo) {
        if (costo < 0)
            throw new IllegalArgumentException(
                "El costo no puede ser negativo. Recibido: " + costo);
    }
}
