package taller.model;

/**
 * Clase abstracta que representa una persona dentro del sistema del taller.
 *
 * Encapsula los atributos comunes a todo individuo registrado:
 * nombre completo y teléfono de contacto.
 *
 * @author Simon Valencia
 * @version 1.0
 */
public abstract class Persona implements Identificable {

    /** Nombre completo de la persona. */
    protected String nombre;

    /** Teléfono de contacto. */
    protected String telefono;


    /**
     * Constructor que inicializa el nombre y telefono
     */
    protected Persona(String nombre, String telefono) {
        Validaciones.requerido(nombre,  "El nombre");
        Validaciones.telefono(telefono);
        this.nombre   = nombre.trim();
        this.telefono = telefono.trim();
    }
    
    /**
     * Retorna el nombre completo de la persona.
     */
    public String getNombre()   { return nombre; }

    /**
     * Retorna el teléfono de la persona.
     */
    public String getTelefono() { return telefono; }

    /**
     * Actualiza el nombre de la persona.
     */
    public void setNombre(String nombre) {
        Validaciones.requerido(nombre, "El nombre");
        this.nombre = nombre.trim();
    }

    /**
     * Actualiza el teléfono de la persona.
     */
    public void setTelefono(String telefono) {
        Validaciones.telefono(telefono);
        this.telefono = telefono.trim();
    }

    /**
     * Cada clase hija define su identificador
     */
    @Override
    public abstract String getId();

    /**
     * Cada clase hija define su descripcion
     */
    @Override
    public abstract String getDescripcion();
}
