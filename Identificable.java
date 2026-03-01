package taller.model;

/**
 * contrato para cualquier entidad del sistema que pueda identificarse
 * de forma única.
 *
 * <p>es implementada por Cliente y Mecanico a través de
 * la clase abstracta Persona, garantizando que ambas entidades
 * exponen un identificador único y a su misma ves una descipcion legible.</p>
 *
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public interface Identificable {

    /**
     * retorna el identificador único de la entidad.
     *
     *
     *   en Cliente: devuelve el número de identificación (cédula/pasaporte).
     *   en Mecanico: devuelve el código interno del mecánico.
     *
     *
     * @return cadena no nula con el identificador único.
     */
    String getId();

    /**
     * retorna una descripción de la entidad.
     *
     * @return cadena descriptiva con los datos más relevantes.
     */
    String getDescripcion();
}
