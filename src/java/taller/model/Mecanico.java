package taller.model;

/**
 * Representa un mecánico del taller de bicicletas.
 *
 * <p>Extiende {@link Persona}, heredando nombre y teléfono, y añade
 * los datos propios del mecánico: especialidad técnica y código interno único.</p>
 *
 * <p>Implementa {@link Identificable} a través de {@link Persona};
 * su identificador único es el código interno del taller.</p>
 *
 * @author Juan Manuel Vera
 * @version 1.0
 * @see Persona
 * @see Identificable
 */
public class Mecanico extends Persona {

    /** Área técnica de especialización (ej: frenos, suspensión, eléctrica). */
    private String especialidad;

    /** Código interno único o número de certificación del mecánico. */
    private String codigo;

    // ── Constructor ────────────────────────────────────────────────────────────

    /**
     * Crea un nuevo mecánico con sus datos de identificación y especialidad.
     *
     * <p>Delega la validación y asignación de {@code nombre} y {@code telefono}
     * al constructor de {@link Persona}.</p>
     *
     * @param nombre       Nombre completo (mínimo 3 caracteres).
     * @param telefono     Teléfono de contacto (7–15 dígitos).
     * @param especialidad Especialidad técnica (no nula, no vacía).
     * @param codigo       Código interno único (no nulo, no vacío).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Mecanico(String nombre, String telefono, String especialidad, String codigo) {
        super(nombre, telefono);                              // hereda nombre y teléfono
        Validaciones.longitudMinima(nombre, 3, "El nombre del mecánico");
        Validaciones.requerido(especialidad, "La especialidad del mecánico");
        Validaciones.requerido(codigo,       "El código del mecánico");
        this.especialidad = especialidad.trim();
        this.codigo       = codigo.trim();
    }

    // ── Identificable ─────────────────────────────────────────────────────────

    /**
     * Retorna el código interno del mecánico como clave única.
     *
     * @return código interno del taller.
     */
    @Override
    public String getId() { return codigo; }

    /**
     * Retorna una descripción legible del mecánico para reportes y UI.
     *
     * @return "Nombre (Especialidad) [Código]".
     */
    @Override
    public String getDescripcion() {
        return nombre + " (" + especialidad + ") [" + codigo + "]";
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    /** @return especialidad técnica del mecánico. */
    public String getEspecialidad() { return especialidad; }

    /** @return código interno único del mecánico. */
    public String getCodigo()       { return codigo; }

    // ── Setters ────────────────────────────────────────────────────────────────

    /** @param especialidad Nueva especialidad (no nula, no vacía). */
    public void setEspecialidad(String especialidad) {
        Validaciones.requerido(especialidad, "La especialidad");
        this.especialidad = especialidad.trim();
    }

    /**
     * @return "Nombre (Especialidad) [Código]" para uso en tablas.
     */
    @Override
    public String toString() {
        return nombre + " (" + especialidad + ") [" + codigo + "]";
    }
}
