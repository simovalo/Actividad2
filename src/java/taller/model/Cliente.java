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

    /** Número de identificación único del cliente (cédula, pasaporte, etc.). */
    private String idCliente;

    /** Dirección de residencia o correspondencia. */
    private String direccion;

    /** Lista de bicicletas asociadas a este cliente. */
    private final List<Bicicleta> bicicletas;

    // ── Constructor ────────────────────────────────────────────────────────────

    /**
     * Crea un nuevo cliente con todos sus datos de contacto.
     *
     * <p>Delega la validación y asignación de {@code nombre} y {@code telefono}
     * al constructor de {@link Persona}.</p>
     *
     * @param nombre    Nombre completo (no nulo, no vacío).
     * @param idCliente Número de identificación único (no nulo, no vacío).
     * @param telefono  Teléfono de contacto (7–15 dígitos).
     * @param direccion Dirección de residencia (no nula, no vacía).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Cliente(String nombre, String idCliente, String telefono, String direccion) {
        super(nombre, telefono);                              // hereda nombre y teléfono
        Validaciones.requerido(idCliente, "La identificación del cliente");
        Validaciones.requerido(direccion, "La dirección del cliente");
        this.idCliente  = idCliente.trim();
        this.direccion  = direccion.trim();
        this.bicicletas = new ArrayList<>();
    }

    // ── Identificable ─────────────────────────────────────────────────────────

    /**
     * Retorna el número de identificación del cliente como clave única.
     *
     * @return idCliente (cédula o pasaporte).
     */
    @Override
    public String getId() { return idCliente; }

    /**
     * Retorna una descripción legible del cliente para reportes y UI.
     *
     * @return "Nombre [ID] — Dirección".
     */
    @Override
    public String getDescripcion() {
        return nombre + " [" + idCliente + "] — " + direccion;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    /** @return número de identificación único del cliente. */
    public String getIdCliente() { return idCliente; }

    /** @return dirección de residencia del cliente. */
    public String getDireccion() { return direccion; }

    /**
     * Retorna una vista no modificable de las bicicletas del cliente.
     *
     * @return lista de bicicletas (solo lectura).
     */
    public List<Bicicleta> getBicicletas() {
        return Collections.unmodifiableList(bicicletas);
    }

    // ── Setters ────────────────────────────────────────────────────────────────

    /** @param direccion Nueva dirección (no nula, no vacía). */
    public void setDireccion(String direccion) {
        Validaciones.requerido(direccion, "La dirección del cliente");
        this.direccion = direccion.trim();
    }

    // ── Operaciones ────────────────────────────────────────────────────────────

    /**
     * Agrega una bicicleta a la lista del cliente.
     * Solo debe invocarse desde {@link TallerBicicletas}.
     *
     * @param b Bicicleta a agregar (no nula).
     */
    public void agregarBicicleta(Bicicleta b) {
        Validaciones.noNulo(b, "La bicicleta");
        bicicletas.add(b);
    }

    /**
     * @return "Nombre [ID]" para uso rápido en tablas y listas.
     */
    @Override
    public String toString() {
        return nombre + " [" + idCliente + "]";
    }
}
