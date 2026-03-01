package taller.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa un cliente registrado en el taller de bicicletas.
 *
 * <p>Extiende {@link Persona}, heredando nombre y teléfono, y añade
 * los datos propios del cliente: número de identificación único,
 * dirección y la lista de bicicletas de su propiedad.</p>
 *
 * <p>Implementa {@link Identificable} a través de {@link Persona};
 * su identificador único es el número de cédula o pasaporte.</p>
 *
 * @author Simon Valencia
 * @version 1.0
 * @see Persona
 * @see Identificable
 */
public class Cliente extends Persona {

    /**
     * atributos.
     */
    private String idCliente;
    private String direccion;
    private final List<Bicicleta> bicicletas;
    private List<TallerBicicletas> ownedByTallerBicicletas;

    //  Constructor

    /**
     * crea un nuevo cliente con todos sus datos de contacto.
     *
     * <p>Delega la validación y asignación de nombre y telefono
     * al constructor de Persona.</p>
     *
     * @param nombre    nombre completo (no nulo, no vacío).
     * @param idCliente número de identificación único (no nulo, no vacío).
     * @param telefono  teléfono de contacto (7–15 dígitos).
     * @param direccion dirección de residencia (no nula, no vacía).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Cliente(String nombre, String idCliente, String telefono, String direccion) {
        super(nombre, telefono);                              // hereda nombre y teléfono
        Validaciones.requerido(idCliente, "la identificación del cliente");
        Validaciones.requerido(direccion, "la dirección del cliente");
        this.idCliente  = idCliente.trim();
        this.direccion  = direccion.trim();
        this.bicicletas = new ArrayList<>();
        this.ownedByTallerBicicletas = new ArrayList<>();
    }

    // Identificable


    @Override
    public String getId() { return idCliente; }

    /**
     * retorna una descripción del cliente para reportes y UI.
     *
     * @return "nombre [ID] — direccion".
     */
    @Override
    public String getDescripcion() {
        return nombre + " [" + idCliente + "] — " + direccion;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getIdCliente() { return idCliente; }
    public String getDireccion() { return direccion; }
    public List<Bicicleta> getBicicletas() {
        return Collections.unmodifiableList(bicicletas);
    }
    public List<TallerBicicletas> getOwnedByTallerBicicletas() {
        return Collections.unmodifiableList(ownedByTallerBicicletas);
    }

    // Setters


    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setDireccion(String direccion) {
        Validaciones.requerido(direccion, "la dirección del cliente");
        this.direccion = direccion.trim();
    }

    //  Operaciones

    /**
     * Agrega una bicicleta a la lista del cliente.
     * Solo debe invocarse desde link TallerBicicletas.
     *
     * @param b bicicleta a agregar (no nula).
     */
    public void agregarBicicleta(Bicicleta b) {
        Validaciones.noNulo(b, "La bicicleta");
        bicicletas.add(b);
    }

    /**
     * @return "nombre [ID]".
     */
    @Override
    public String toString() {
        return nombre + " [" + idCliente + "]";
    }
}
