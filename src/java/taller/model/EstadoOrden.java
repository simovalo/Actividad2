package taller.model;

/**
 * Representa el estado actual de una orden de servicio en el taller.
 *
 * <p>Una orden avanza de forma secuencial: inicia como {@code PENDIENTE}
 * cuando se crea, pasa a {@code EN_PROCESO} cuando el mecánico comienza
 * el trabajo, y termina como {@code COMPLETADA} al entregar la bicicleta.</p>
 *
 * @author Samuel Marin
 * @version 1.0
 */
public enum EstadoOrden {

    /** La orden fue creada pero aún no ha sido atendida por el mecánico. */
    PENDIENTE("Pendiente"),

    /** El mecánico está trabajando actualmente en la bicicleta. */
    EN_PROCESO("En proceso"),

    /** El servicio fue finalizado y la bicicleta está lista para entregar. */
    COMPLETADA("Completada");

    // ── Atributo ───────────────────────────────────────────────────────────────

    /** Etiqueta legible para mostrar en la interfaz gráfica. */
    private final String etiqueta;

    // ── Constructor del enum ───────────────────────────────────────────────────

    /**
     * Asocia una etiqueta legible a cada valor del enum.
     *
     * @param etiqueta Texto descriptivo para mostrar en la UI.
     */
    EstadoOrden(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // ── Getter ─────────────────────────────────────────────────────────────────

    /**
     * Retorna la etiqueta legible del estado.
     *
     * @return texto del estado para la interfaz.
     */
    public String getEtiqueta() { return etiqueta; }

    /**
     * Retorna la etiqueta como representación textual del enum.
     *
     * @return la etiqueta del estado.
     */
    @Override
    public String toString() { return etiqueta; }
}
