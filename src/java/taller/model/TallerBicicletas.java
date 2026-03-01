package taller.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase central del sistema. Representa el taller como entidad con identidad
 * propia (nombre, dirección, teléfono) y gestiona todas las colecciones y
 * la lógica de negocio.
 *
 * <p>Es el único punto de acceso para crear y relacionar entidades del modelo,
 * garantizando las reglas de integridad antes de persistir cualquier objeto.</p>
 *
 * <p>Expone el método genérico {@link #buscarPersona(List, String)} que opera
 * sobre cualquier {@link Identificable}, aprovechando el polimorfismo para
 * reutilizar la lógica de búsqueda en clientes y mecánicos.</p>
 *
 * @author Simon Valencia
 * @version 1.0
 */
public class TallerBicicletas {

    // ── Datos del taller ───────────────────────────────────────────────────────

    /** Nombre comercial del taller. */
    private String nombre;

    /** Dirección física del taller. */
    private String direccion;

    /** Teléfono de contacto del taller. */
    private String telefono;

    // ── Colecciones ────────────────────────────────────────────────────────────

    private final List<Cliente>       clientes   = new ArrayList<>();
    private final List<Bicicleta>     bicicletas = new ArrayList<>();
    private final List<Mecanico>      mecanicos  = new ArrayList<>();
    private final List<OrdenServicio> ordenes    = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────────────────────

    /**
     * Crea el taller con nombre, dirección y teléfono.
     *
     * @param nombre    Nombre comercial (no nulo, no vacío).
     * @param direccion Dirección física (no nula, no vacía).
     * @param telefono  Teléfono de contacto (7–15 dígitos).
     */
    public TallerBicicletas(String nombre, String direccion, String telefono) {
        Validaciones.requerido(nombre,    "El nombre del taller");
        Validaciones.requerido(direccion, "La dirección del taller");
        Validaciones.telefono(telefono);
        this.nombre    = nombre.trim();
        this.direccion = direccion.trim();
        this.telefono  = telefono.trim();
    }

    /** Constructor de conveniencia para pruebas unitarias. */
    public TallerBicicletas() {
        this("Taller de Bicicletas", "Dirección por defecto", "3001234567");
    }

    // ── Getters / Setters del taller ──────────────────────────────────────────

    /** @return nombre comercial del taller. */
    public String getNombre()    { return nombre; }

    /** @return dirección física del taller. */
    public String getDireccion() { return direccion; }

    /** @return teléfono de contacto. */
    public String getTelefono()  { return telefono; }

    /** @param nombre Nuevo nombre (no nulo, no vacío). */
    public void setNombre(String nombre) {
        Validaciones.requerido(nombre, "El nombre del taller");
        this.nombre = nombre.trim();
    }

    /** @param direccion Nueva dirección (no nula, no vacía). */
    public void setDireccion(String direccion) {
        Validaciones.requerido(direccion, "La dirección del taller");
        this.direccion = direccion.trim();
    }

    /** @param telefono Nuevo teléfono (7–15 dígitos). */
    public void setTelefono(String telefono) {
        Validaciones.telefono(telefono);
        this.telefono = telefono.trim();
    }

    // ── F1 — Registrar Cliente ─────────────────────────────────────────────────

    /**
     * Registra un nuevo cliente en el taller.
     *
     * @param nombre    Nombre completo.
     * @param id        Identificación única.
     * @param telefono  Teléfono de contacto.
     * @param direccion Dirección.
     * @return El {@link Cliente} creado.
     * @throws IllegalArgumentException si el ID ya existe o algún dato es inválido.
     */
    public Cliente registrarCliente(String nombre, String id,
                                    String telefono, String direccion) {
        if (buscarCliente(id) != null)
            throw new IllegalArgumentException("Ya existe un cliente con ID: " + id);
        Cliente c = new Cliente(nombre, id, telefono, direccion);
        clientes.add(c);
        return c;
    }

    // ── F2 — Registrar Bicicleta ───────────────────────────────────────────────

    /**
     * Registra una bicicleta y la asocia al cliente indicado.
     *
     * @param serial    Serial/marco único.
     * @param marca     Marca.
     * @param tipo      Tipo de bicicleta ({@link Bicicleta.Tipo}).
     * @param color     Color.
     * @param anio      Año de fabricación.
     * @param idCliente ID del cliente propietario.
     * @return La {@link Bicicleta} creada.
     * @throws IllegalArgumentException si el serial ya existe o el cliente no existe.
     */
    public Bicicleta registrarBicicleta(String serial, String marca,
                                        Bicicleta.Tipo tipo, String color,
                                        int anio, String idCliente) {
        if (buscarBicicleta(serial) != null)
            throw new IllegalArgumentException("Ya existe una bicicleta con serial: " + serial);
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null)
            throw new IllegalArgumentException("No se encontró cliente con ID: " + idCliente);
        Bicicleta b = new Bicicleta(serial, marca, tipo, color, anio, cliente);
        bicicletas.add(b);
        cliente.agregarBicicleta(b);
        return b;
    }

    // ── F3 — Registrar Mecánico ────────────────────────────────────────────────

    /**
     * Registra un nuevo mecánico en el taller.
     *
     * @param nombre       Nombre completo (mínimo 3 caracteres).
     * @param telefono     Teléfono de contacto (7–15 dígitos).
     * @param especialidad Área de especialización.
     * @param codigo       Código interno único.
     * @return El {@link Mecanico} creado.
     * @throws IllegalArgumentException si el código ya existe o algún dato es inválido.
     */
    public Mecanico registrarMecanico(String nombre, String telefono,
                                      String especialidad, String codigo) {
        if (buscarMecanico(codigo) != null)
            throw new IllegalArgumentException("Ya existe un mecánico con código: " + codigo);
        Mecanico m = new Mecanico(nombre, telefono, especialidad, codigo);
        mecanicos.add(m);
        return m;
    }

    // ── F4 — Crear Orden de Servicio ───────────────────────────────────────────

    /**
     * Crea una orden de servicio y la marca como {@link EstadoOrden#PENDIENTE}.
     *
     * @param serial         Serial de la bicicleta a atender.
     * @param codigoMecanico Código del mecánico responsable.
     * @param fecha          Fecha de ingreso (no puede ser futura).
     * @param hora           Hora de ingreso.
     * @param motivo         Motivo declarado por el cliente.
     * @param diagnostico    Diagnóstico técnico.
     * @param trabajos       Trabajos realizados.
     * @param costo          Costo total (≥ 0).
     * @return La {@link OrdenServicio} creada con número secuencial asignado.
     */
    public OrdenServicio crearOrden(String serial, String codigoMecanico,
                                    LocalDate fecha, LocalTime hora,
                                    String motivo, String diagnostico,
                                    String trabajos, double costo) {
        Bicicleta b = buscarBicicleta(serial);
        if (b == null)
            throw new IllegalArgumentException("No se encontró bicicleta con serial: " + serial);
        Mecanico m = buscarMecanico(codigoMecanico);
        if (m == null)
            throw new IllegalArgumentException("No se encontró mecánico con código: " + codigoMecanico);
        OrdenServicio o = new OrdenServicio(fecha, hora, b, m, motivo, diagnostico, trabajos, costo);
        ordenes.add(o);
        return o;
    }

    // ── F5 — Historial de bicicleta ────────────────────────────────────────────

    /**
     * @param serial Serial a consultar.
     * @return Lista (puede estar vacía) de órdenes del serial.
     */
    public List<OrdenServicio> historialBicicleta(String serial) {
        return ordenes.stream()
                .filter(o -> o.getBicicleta().getSerial().equalsIgnoreCase(serial))
                .collect(Collectors.toList());
    }

    // ── F6 — Órdenes por fecha ─────────────────────────────────────────────────

    /**
     * @param fecha Fecha a consultar (no nula).
     * @return Lista (puede estar vacía) de órdenes de esa fecha.
     */
    public List<OrdenServicio> ordenesPorFecha(LocalDate fecha) {
        Validaciones.noNulo(fecha, "La fecha de consulta");
        return ordenes.stream()
                .filter(o -> o.getFechaIngreso().equals(fecha))
                .collect(Collectors.toList());
    }

    // ── F7 — Reporte de costos por cliente ────────────────────────────────────

    /**
     * @param idCliente ID del cliente.
     * @return Suma de costos (0.0 si no tiene órdenes).
     * @throws IllegalArgumentException si el cliente no existe.
     */
    public double costosAcumuladosPorCliente(String idCliente) {
        if (buscarCliente(idCliente) == null)
            throw new IllegalArgumentException("No se encontró cliente con ID: " + idCliente);
        return ordenes.stream()
                .filter(o -> o.getBicicleta().getPropietario().getIdCliente().equals(idCliente))
                .mapToDouble(OrdenServicio::getCostoTotal)
                .sum();
    }

    // ── Búsquedas ──────────────────────────────────────────────────────────────

    /**
     * Método genérico que busca una {@link Persona} ({@link Identificable})
     * en cualquier lista por su ID único.
     *
     * <p>Aprovecha el polimorfismo de la interfaz {@link Identificable}
     * para reutilizar la misma lógica de búsqueda en clientes y mecánicos.</p>
     *
     * @param <T>   Tipo que extiende {@link Persona} e implementa {@link Identificable}.
     * @param lista Lista donde buscar.
     * @param id    Identificador a buscar (no sensible a mayúsculas).
     * @return El elemento encontrado, o {@code null} si no existe.
     */
    public <T extends Persona> T buscarPersona(List<T> lista, String id) {
        return lista.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Busca un cliente por su número de identificación.
     *
     * @param id Identificación (no sensible a mayúsculas).
     * @return El {@link Cliente} o {@code null} si no existe.
     */
    public Cliente buscarCliente(String id) {
        return buscarPersona(clientes, id);
    }

    /**
     * Busca una bicicleta por su serial.
     *
     * @param serial Serial (no sensible a mayúsculas).
     * @return La {@link Bicicleta} o {@code null} si no existe.
     */
    public Bicicleta buscarBicicleta(String serial) {
        return bicicletas.stream()
                .filter(b -> b.getSerial().equalsIgnoreCase(serial))
                .findFirst().orElse(null);
    }

    /**
     * Busca un mecánico por su código interno.
     *
     * @param codigo Código (no sensible a mayúsculas).
     * @return El {@link Mecanico} o {@code null} si no existe.
     */
    public Mecanico buscarMecanico(String codigo) {
        return buscarPersona(mecanicos, codigo);
    }

    // ── Getters de colecciones ─────────────────────────────────────────────────

    /** @return Vista no modificable de la lista de clientes. */
    public List<Cliente>       getClientes()   { return Collections.unmodifiableList(clientes); }

    /** @return Vista no modificable de la lista de bicicletas. */
    public List<Bicicleta>     getBicicletas() { return Collections.unmodifiableList(bicicletas); }

    /** @return Vista no modificable de la lista de mecánicos. */
    public List<Mecanico>      getMecanicos()  { return Collections.unmodifiableList(mecanicos); }

    /** @return Vista no modificable de la lista de órdenes. */
    public List<OrdenServicio> getOrdenes()    { return Collections.unmodifiableList(ordenes); }

    /** @return nombre, dirección y teléfono del taller. */
    @Override
    public String toString() {
        return nombre + " | " + direccion + " | Tel: " + telefono;
    }
}
