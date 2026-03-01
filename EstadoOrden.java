package taller.model;

/**
 * representa el estado actual de una orden .
 *
 * <p>una orden avanza de forma secuencial: inicia como PENDIENTE
 * cuando se crea, pasa a EN_PROCESO cuando el mecanico comienza
 * el trabajo, y termina como @code COMPLETADA al entregar la bicicleta.</p>
 *
 * @author Samuel Marin
 * @version 1.0
 */
public enum EstadoOrden {

    /** la orden fue creada pero aún no ha sido atendida por el mecánico. */
    PENDIENTE("pendiente"),

    /** el mecánico está trabajando actualmente. */
    EN_PROCESO("en proceso"),

    /** el servicio fue finalizado. */
    COMPLETADA("completada");

    // Atributo

    /** etiqueta legible. */
    private final String etiqueta;

    // Constructor del enum

    /**
     * asocio una etiqueta legible a cada valor del enum.
     *
     * @param etiqueta texto para mostrar en la UI.
     */
    EstadoOrden(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    //  Getter

    public String getEtiqueta() { return etiqueta; }

    /**
     * retorna la etiqueta como representación textual.
     *
     * @return la etiqueta .
     */
    @Override
    public String toString() { return etiqueta; }
}
