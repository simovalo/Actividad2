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
 * @author Simon Valencia
 * @version 1.0
 */
public class TallerBicicletas {

    private String nombre;
    private String direccion;
    private String telefono;

    private final List<Cliente>       clientes   = new ArrayList<>();
    private final List<Bicicleta>     bicicletas = new ArrayList<>();
    private final List<Mecanico>      mecanicos  = new ArrayList<>();
    private final List<OrdenServicio> ordenes    = new ArrayList<>();


    /**
     * Crea el taller con nombre, dirección y teléfono.
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

    /** @return nombre comercial del taller. */
    public String getNombre()    { return nombre; }

    /** @return dirección física del taller. */
    public String getDireccion() { return direccion; }

    /** @return teléfono de contacto. */
    public String getTelefono()  { return telefono; }

    /** @param nombre Nuevo nombre. */
    public void setNombre(String nombre) {
        Validaciones.requerido(nombre, "El nombre del taller");
        this.nombre = nombre.trim();
    }

    /** @param direccion Nueva dirección. */
    public void setDireccion(String direccion) {
        Validaciones.requerido(direccion, "La dirección del taller");
        this.direccion = direccion.trim();
    }

    /** @param telefono Nuevo teléfono. */
    public void setTelefono(String telefono) {
        Validaciones.telefono(telefono);
        this.telefono = telefono.trim();
    }

    /**
     * Registra un nuevo cliente en el taller.
     */
    public Cliente registrarCliente(String nombre, String id,
                                    String telefono, String direccion) {
        if (buscarCliente(id) != null)
            throw new IllegalArgumentException("Ya existe un cliente con ID: " + id);
        Cliente c = new Cliente(nombre, id, telefono, direccion);
        clientes.add(c);
        return c;
    }
    
    /**
     * Registra una bicicleta y la asocia al cliente indicado.
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
    
    /**
     * Registra un nuevo mecánico en el taller.
     */
    public Mecanico registrarMecanico(String nombre, String telefono,
                                      String especialidad, String codigo) {
        if (buscarMecanico(codigo) != null)
            throw new IllegalArgumentException("Ya existe un mecánico con código: " + codigo);
        Mecanico m = new Mecanico(nombre, telefono, especialidad, codigo);
        mecanicos.add(m);
        return m;
    }

    /**
     * Crea una orden de servicio y la marca como {@link EstadoOrden#PENDIENTE}.
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

    /**
     * Devuelve todas las ordenes de una bicicleta
     */
    public List<OrdenServicio> historialBicicleta(String serial) {
        return ordenes.stream()
                .filter(o -> o.getBicicleta().getSerial().equalsIgnoreCase(serial))
                .collect(Collectors.toList());
    }

    /**
     * Devuelve las ordenes registradas en una fecha especifica
     */
    public List<OrdenServicio> ordenesPorFecha(LocalDate fecha) {
        Validaciones.noNulo(fecha, "La fecha de consulta");
        return ordenes.stream()
                .filter(o -> o.getFechaIngreso().equals(fecha))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el total gastado por un cliente en ordenes
     */
    public double costosAcumuladosPorCliente(String idCliente) {
        if (buscarCliente(idCliente) == null)
            throw new IllegalArgumentException("No se encontró cliente con ID: " + idCliente);
        return ordenes.stream()
                .filter(o -> o.getBicicleta().getPropietario().getIdCliente().equals(idCliente))
                .mapToDouble(OrdenServicio::getCostoTotal)
                .sum();
    }

    /**
     * Busca un cliente por su id
     */
    public <T extends Persona> T buscarPersona(List<T> lista, String id) {
        return lista.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Busca un cliente por su número de identificación.
     */
    public Cliente buscarCliente(String id) {
        return buscarPersona(clientes, id);
    }

    /**
     * Busca una bicicleta por su serial.
     */
    public Bicicleta buscarBicicleta(String serial) {
        return bicicletas.stream()
                .filter(b -> b.getSerial().equalsIgnoreCase(serial))
                .findFirst().orElse(null);
    }

    /**
     * Busca un mecánico por su código interno.
     */
    public Mecanico buscarMecanico(String codigo) {
        return buscarPersona(mecanicos, codigo);
    }

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
