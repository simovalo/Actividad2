package taller.model;

/**
 * Clase abstracta que representa una persona dentro del sistema del taller.
 *
 * <p>Encapsula los atributos comunes a todo individuo registrado:
 * nombre completo y teléfono de contacto. Implementa la interfaz
 * {@link Identificable} de forma parcial, dejando {@link #getId()} y
 * {@link #getDescripcion()} como métodos abstractos para que cada
 * subclase los defina según su propia semántica de identidad.</p>
 *
 * <p>Las subclases concretas son:</p>
 * <ul>
 *   <li>{@link Cliente} — añade identificación, dirección y bicicletas.</li>
 *   <li>{@link Mecanico} — añade especialidad y código interno.</li>
 * </ul>
 *
 * @author Simon Valencia
 * @version 1.0
 */
public abstract class Persona implements Identificable {

    /** Nombre completo de la persona. No puede ser nulo ni vacío. */
    protected String nombre;

    /** Teléfono de contacto. Solo dígitos, entre 7 y 15 caracteres. */
    protected String telefono;

    // ── Constructor ────────────────────────────────────────────────────────────

    /**
     * Inicializa los datos comunes de una persona.
     *
     * @param nombre   Nombre completo (no nulo, no vacío).
     * @param telefono Teléfono de contacto (7–15 dígitos).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    protected Persona(String nombre, String telefono) {
        Validaciones.requerido(nombre,  "El nombre");
        Validaciones.telefono(telefono);
        this.nombre   = nombre.trim();
        this.telefono = telefono.trim();
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    /**
     * Retorna el nombre completo de la persona.
     *
     * @return nombre de la persona.
     */
    public String getNombre()   { return nombre; }

    /**
     * Retorna el teléfono de contacto de la persona.
     *
     * @return teléfono de la persona.
     */
    public String getTelefono() { return telefono; }

    // ── Setters ────────────────────────────────────────────────────────────────

    /**
     * Actualiza el nombre de la persona.
     *
     * @param nombre Nuevo nombre (no nulo, no vacío).
     */
    public void setNombre(String nombre) {
        Validaciones.requerido(nombre, "El nombre");
        this.nombre = nombre.trim();
    }

    /**
     * Actualiza el teléfono de la persona.
     *
     * @param telefono Nuevo teléfono (7–15 dígitos).
     */
    public void setTelefono(String telefono) {
        Validaciones.telefono(telefono);
        this.telefono = telefono.trim();
    }

    // ── Métodos abstractos (contrato Identificable) ────────────────────────────

    /**
     * {@inheritDoc}
     * <p>Cada subclase define qué campo actúa como identificador único.</p>
     */
    @Override
    public abstract String getId();

    /**
     * {@inheritDoc}
     * <p>Cada subclase produce su propia representación descriptiva.</p>
     */
    @Override
    public abstract String getDescripcion();
}
