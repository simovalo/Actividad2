package taller.model;

/**
 * Contrato para cualquier entidad del sistema que pueda identificarse
 * de forma única y describirse a sí misma.
 *
 * <p>Es implementada por {@link Cliente} y {@link Mecanico} a través de
 * la clase abstracta {@link Persona}, garantizando que ambas entidades
 * exponen un identificador único y una descripción legible.</p>
 *
 * <p>Permite tratar de forma polimórfica a clientes y mecánicos
 * en operaciones genéricas de búsqueda o reporte.</p>
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public interface Identificable {

    /**
     * Retorna el identificador único de la entidad.
     *
     * <ul>
     *   <li>En {@link Cliente}: devuelve el número de identificación (cédula/pasaporte).</li>
     *   <li>En {@link Mecanico}: devuelve el código interno del mecánico.</li>
     * </ul>
     *
     * @return cadena no nula con el identificador único.
     */
    String getId();

    /**
     * Retorna una descripción legible de la entidad para uso en UI y reportes.
     *
     * @return cadena descriptiva con los datos más relevantes de la entidad.
     */
    String getDescripcion();
}
