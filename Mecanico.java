package taller.model;

import java.util.List;

/**
 * Representa un mecánico del taller de bicicletas.
 *
 * <p>extiende Persona, heredando nombre y teléfono, y añade
 * los datos propios del mecánico como: especialidad técnica y código interno único.</p>
 *
 * <p>implementa Identificable a través de Persona;
 * su identificador único es el código interno del taller.</p>
 *
 * @author Juan Manuel Vera
 * @version 1.0
 * @see Persona
 * @see Identificable
 */
public class Mecanico extends Persona {

    private String especialidad;
    private String codigo;
    private List<TallerBicicletas> ownedByTallerBicicletas;

    // Constructor

    /**
     * crea un nuevo mecánico con sus datos de identificación y especialidad.
     *
     * <p>delega la validación y asignación de nombre y telefono
     * al constructor de Persona.</p>
     *
     * @param nombre       nombre completo (mínimo 3 caracteres).
     * @param telefono     telefono de contacto (7–15 dígitos).
     * @param especialidad especialidad técnica (no nula, no vacía).
     * @param codigo       codigo interno único (no nulo, no vacío).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Mecanico(String nombre, String telefono, String especialidad, String codigo) {
        super(nombre, telefono);                              // hereda
        Validaciones.longitudMinima(nombre, 3, "el nombre del mecánico");
        Validaciones.requerido(especialidad, "la especialidad del mecánico");
        Validaciones.requerido(codigo,       "el código del mecánico");
        this.especialidad = especialidad.trim();
        this.codigo       = codigo.trim();
        this.ownedByTallerBicicletas = List.of();
    }

    // Identificable

    /**
     * retorna el código interno del mecánico como clave única.
     *
     * @return código interno del taller.
     */
    @Override
    public String getId() { return codigo; }

    /**
     * retorna una descripción del mecánico.
     *
     * @return "nombre (especialidad) [codigo]".
     */
    @Override
    public String getDescripcion() {
        return nombre + " (" + especialidad + ") [" + codigo + "]";
    }

    // Getters

    public String getEspecialidad() { return especialidad; }
    public String getCodigo()       { return codigo; }
    public List<TallerBicicletas> getOwnedByTallerBicicletas() { return ownedByTallerBicicletas; }

    // Setters

    /** @param especialidad nueva especialidad (no nula, no vacía). */
    public void setEspecialidad(String especialidad) {
        Validaciones.requerido(especialidad, "La especialidad");
        this.especialidad = especialidad.trim();
    }

    /**
     * @return "nombre (especialidad) [codigo]" para uso en tablas.
     */
    @Override
    public String toString() {
        return nombre + " (" + especialidad + ") [" + codigo + "]";
    }
}
