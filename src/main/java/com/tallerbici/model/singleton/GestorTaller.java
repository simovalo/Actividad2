package com.tallerbici.model.singleton;

import com.tallerbici.model.*;
import com.tallerbici.model.enums.EstadoOrden;
import com.tallerbici.model.enums.TipoBicicleta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Repositorio central del taller. Patrón SINGLETON.
 *
 * Guarda todos los datos en ObservableLists.
 * Una ObservableList es igual a un ArrayList normal, pero también
 * avisa automáticamente a la interfaz gráfica cuando cambia algo,
 * así el ListView y el ComboBox se actualizan solos.
 *
 * Para buscar un elemento se recorre la lista con un for.
 * Es más simple que un HashMap y suficiente para este proyecto.
 *
 * SINGLETON: solo puede existir UN GestorTaller en toda la ejecución.
 *   - Constructor privado → nadie puede hacer "new GestorTaller()" desde afuera.
 *   - getInstance() → devuelve siempre el mismo objeto.
 *
 * @author Equipo TallerBici - Programación II
 */
public class GestorTaller {

    // ============================================================
    // SINGLETON
    // ============================================================

    // La única instancia que existirá. Empieza en null.
    // volatile: garantiza que todos los hilos del programa ven el mismo valor.
    private static volatile GestorTaller instancia;

    // Constructor PRIVADO: nadie fuera de esta clase puede crear un GestorTaller.
    private GestorTaller() {
        clientes   = FXCollections.observableArrayList();
        bicicletas = FXCollections.observableArrayList();
        mecanicos  = FXCollections.observableArrayList();
        ordenes    = FXCollections.observableArrayList();
        repuestos  = FXCollections.observableArrayList();
        cargarDatosDeMuestra();
    }

    /**
     * Devuelve siempre el mismo GestorTaller.
     * Si todavía no existe, lo crea. Si ya existe, devuelve el que hay.
     *
     * Double-Checked Locking: revisa dos veces antes de crear,
     * para que sea seguro si dos hilos llaman a getInstance() al mismo tiempo.
     */
    public static GestorTaller getInstance() {
        if (instancia == null) {
            synchronized (GestorTaller.class) {
                if (instancia == null) {
                    instancia = new GestorTaller();
                }
            }
        }
        return instancia;
    }

    // ============================================================
    // LISTAS DE DATOS
    // Una ObservableList funciona igual que un ArrayList,
    // pero además notifica a ListView y ComboBox cuando cambia.
    // ============================================================

    private ObservableList<Cliente>       clientes;
    private ObservableList<Bicicleta>     bicicletas;
    private ObservableList<Mecanico>      mecanicos;
    private ObservableList<OrdenServicio> ordenes;
    private ObservableList<Repuesto>      repuestos;

    // Número para generar IDs de órdenes: ORD-0001, ORD-0002...
    private int contadorOrdenes = 1;

    // ============================================================
    // GETTERS DE LISTAS
    // Los controladores usan estas listas para conectar sus controles.
    // ============================================================

    public ObservableList<Cliente>       getListaClientes()   { return clientes; }
    public ObservableList<Bicicleta>     getListaBicicletas() { return bicicletas; }
    public ObservableList<Mecanico>      getListaMecanicos()  { return mecanicos; }
    public ObservableList<OrdenServicio> getListaOrdenes()    { return ordenes; }
    public ObservableList<Repuesto>      getListaRepuestos()  { return repuestos; }

    // ============================================================
    // CLIENTES
    // ============================================================

    public void registrarCliente(Cliente nuevo) {
        // Revisar si ya existe un cliente con esa cédula
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getNumeroCedula().equals(nuevo.getNumeroCedula())) {
                throw new IllegalArgumentException("Ya existe un cliente con cédula: " + nuevo.getNumeroCedula());
            }
        }
        clientes.add(nuevo);
    }

    public void actualizarCliente(Cliente actualizado) {
        // Buscar el cliente por cédula y reemplazarlo
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getNumeroCedula().equals(actualizado.getNumeroCedula())) {
                clientes.set(i, actualizado); // set() reemplaza y notifica a la UI
                return;
            }
        }
        throw new IllegalArgumentException("No existe cliente con cédula: " + actualizado.getNumeroCedula());
    }

    // Busca un cliente por cédula. Devuelve null si no lo encuentra.
    public Cliente buscarCliente(String cedula) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getNumeroCedula().equals(cedula)) {
                return clientes.get(i);
            }
        }
        return null;
    }

    public boolean existeCliente(String cedula) {
        return buscarCliente(cedula) != null;
    }

    // ============================================================
    // BICICLETAS
    // ============================================================

    public void registrarBicicleta(Bicicleta nueva) {
        if (!existeCliente(nueva.getCedulaCliente())) {
            throw new IllegalArgumentException("No existe cliente con cédula: " + nueva.getCedulaCliente());
        }
        for (int i = 0; i < bicicletas.size(); i++) {
            if (bicicletas.get(i).getNumeroSerial().equals(nueva.getNumeroSerial())) {
                throw new IllegalArgumentException("Ya existe una bicicleta con serial: " + nueva.getNumeroSerial());
            }
        }
        bicicletas.add(nueva);
    }

    public void actualizarBicicleta(Bicicleta actualizada) {
        for (int i = 0; i < bicicletas.size(); i++) {
            if (bicicletas.get(i).getNumeroSerial().equals(actualizada.getNumeroSerial())) {
                bicicletas.set(i, actualizada);
                return;
            }
        }
        throw new IllegalArgumentException("No existe bicicleta con serial: " + actualizada.getNumeroSerial());
    }

    // Busca una bicicleta por serial. Devuelve null si no la encuentra.
    public Bicicleta buscarBicicleta(String serial) {
        for (int i = 0; i < bicicletas.size(); i++) {
            if (bicicletas.get(i).getNumeroSerial().equals(serial)) {
                return bicicletas.get(i);
            }
        }
        return null;
    }

    // Devuelve todas las bicicletas que pertenecen a un cliente.
    public ArrayList<Bicicleta> getBicicletasDeCliente(String cedula) {
        ArrayList<Bicicleta> resultado = new ArrayList<>();
        for (int i = 0; i < bicicletas.size(); i++) {
            if (bicicletas.get(i).getCedulaCliente().equals(cedula)) {
                resultado.add(bicicletas.get(i));
            }
        }
        return resultado;
    }

    // ============================================================
    // MECÁNICOS
    // ============================================================

    public void registrarMecanico(Mecanico nuevo) {
        for (int i = 0; i < mecanicos.size(); i++) {
            if (mecanicos.get(i).getCodigoInterno().equals(nuevo.getCodigoInterno())) {
                throw new IllegalArgumentException("Ya existe un mecánico con código: " + nuevo.getCodigoInterno());
            }
        }
        mecanicos.add(nuevo);
    }

    public void actualizarMecanico(Mecanico actualizado) {
        for (int i = 0; i < mecanicos.size(); i++) {
            if (mecanicos.get(i).getCodigoInterno().equals(actualizado.getCodigoInterno())) {
                mecanicos.set(i, actualizado);
                return;
            }
        }
        throw new IllegalArgumentException("No existe mecánico con código: " + actualizado.getCodigoInterno());
    }

    // Busca un mecánico por código. Devuelve null si no lo encuentra.
    public Mecanico buscarMecanico(String codigo) {
        for (int i = 0; i < mecanicos.size(); i++) {
            if (mecanicos.get(i).getCodigoInterno().equals(codigo)) {
                return mecanicos.get(i);
            }
        }
        return null;
    }

    // ============================================================
    // ÓRDENES DE SERVICIO
    // ============================================================

    // Crea una nueva orden. El ID se genera automáticamente.
    public OrdenServicio crearOrden(String serialBici, String codMecanico, String motivo) {
        if (buscarBicicleta(serialBici) == null) {
            throw new IllegalArgumentException("No existe bicicleta con serial: " + serialBici);
        }
        if (buscarMecanico(codMecanico) == null) {
            throw new IllegalArgumentException("No existe mecánico con código: " + codMecanico);
        }

        String idOrden = String.format("ORD-%04d", contadorOrdenes);
        contadorOrdenes++;

        OrdenServicio orden = new OrdenServicio.Builder()
                .idOrden(idOrden)
                .serialBicicleta(serialBici)
                .codigoMecanico(codMecanico)
                .motivoServicio(motivo)
                .build();

        ordenes.add(orden);
        return orden;
    }

    public void actualizarOrden(OrdenServicio actualizada) {
        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getIdOrden().equals(actualizada.getIdOrden())) {
                ordenes.set(i, actualizada);
                return;
            }
        }
        throw new IllegalArgumentException("No existe orden con ID: " + actualizada.getIdOrden());
    }

    public OrdenServicio buscarOrden(String id) {
        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getIdOrden().equals(id)) {
                return ordenes.get(i);
            }
        }
        return null;
    }

    /**
     * Devuelve todas las órdenes de una bicicleta, de más reciente a más antigua.
     */
    public ArrayList<OrdenServicio> getHistorialBicicleta(String serial) {
        // Paso 1: recoger las órdenes que son de esa bicicleta
        ArrayList<OrdenServicio> resultado = new ArrayList<>();
        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getSerialBicicleta().equalsIgnoreCase(serial)) {
                resultado.add(ordenes.get(i));
            }
        }

        // Paso 2: ordenar de más reciente a más antigua con burbuja
        for (int i = 0; i < resultado.size() - 1; i++) {
            for (int j = 0; j < resultado.size() - 1 - i; j++) {
                LocalDate fechaA = resultado.get(j).getFechaIngreso();
                LocalDate fechaB = resultado.get(j + 1).getFechaIngreso();
                // Si fechaA es antes que fechaB, las intercambiamos
                if (fechaA.isBefore(fechaB)) {
                    OrdenServicio temp = resultado.get(j);
                    resultado.set(j, resultado.get(j + 1));
                    resultado.set(j + 1, temp);
                }
            }
        }
        return resultado;
    }

    /**
     * Devuelve todas las órdenes ingresadas en una fecha específica.
     */
    public ArrayList<OrdenServicio> getOrdenesPorFecha(LocalDate fecha) {
        ArrayList<OrdenServicio> resultado = new ArrayList<>();
        for (int i = 0; i < ordenes.size(); i++) {
            if (ordenes.get(i).getFechaIngreso().equals(fecha)) {
                resultado.add(ordenes.get(i));
            }
        }
        return resultado;
    }

    // ============================================================
    // REPUESTOS
    // ============================================================

    public void registrarRepuesto(Repuesto nuevo) {
        for (int i = 0; i < repuestos.size(); i++) {
            if (repuestos.get(i).getCodigo().equals(nuevo.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un repuesto con código: " + nuevo.getCodigo());
            }
        }
        repuestos.add(nuevo);
    }

    public void actualizarRepuesto(Repuesto actualizado) {
        for (int i = 0; i < repuestos.size(); i++) {
            if (repuestos.get(i).getCodigo().equals(actualizado.getCodigo())) {
                repuestos.set(i, actualizado);
                return;
            }
        }
        throw new IllegalArgumentException("No existe repuesto con código: " + actualizado.getCodigo());
    }

    public Repuesto buscarRepuesto(String codigo) {
        for (int i = 0; i < repuestos.size(); i++) {
            if (repuestos.get(i).getCodigo().equals(codigo)) {
                return repuestos.get(i);
            }
        }
        return null;
    }

    /**
     * Devuelve los repuestos que tienen stock menor o igual al mínimo permitido.
     */
    public ArrayList<Repuesto> getRepuestosConStockBajo() {
        ArrayList<Repuesto> resultado = new ArrayList<>();
        for (int i = 0; i < repuestos.size(); i++) {
            if (repuestos.get(i).tieneStockBajo()) {
                resultado.add(repuestos.get(i));
            }
        }
        return resultado;
    }

    // ============================================================
    // DATOS DE MUESTRA
    // ============================================================

    private void cargarDatosDeMuestra() {
        // Clientes de muestra
        Cliente c1 = new Cliente.Builder()
                .numeroCedula("1012345678")
                .nombreCompleto("Carlos Andrés Gómez")
                .telefono("3001234567")
                .direccion("Cra 15 #23-45, Bogotá")
                .build();
        Cliente c2 = new Cliente.Builder()
                .numeroCedula("1098765432")
                .nombreCompleto("María Fernanda López")
                .telefono("3109876543")
                .direccion("Cll 45 #12-30, Medellín")
                .build();
        clientes.add(c1);
        clientes.add(c2);

        // Mecánicos de muestra
        Mecanico m1 = new Mecanico.Builder()
                .codigoInterno("MEC-001")
                .nombreCompleto("Javier Hernández")
                .especialidad("Frenos y Transmisión")
                .numeroCertificacion("CERT-2021-001")
                .build();
        Mecanico m2 = new Mecanico.Builder()
                .codigoInterno("MEC-002")
                .nombreCompleto("Santiago Ruiz")
                .especialidad("Bicicletas Eléctricas")
                .numeroCertificacion("CERT-2022-015")
                .build();
        mecanicos.add(m1);
        mecanicos.add(m2);

        // Bicicletas de muestra
        Bicicleta b1 = new Bicicleta.Builder()
                .numeroSerial("TREK-2021-001")
                .marca("Trek")
                .tipo(TipoBicicleta.MTB)
                .color("Negro")
                .anio(2021)
                .cedulaCliente("1012345678")
                .build();
        Bicicleta b2 = new Bicicleta.Builder()
                .numeroSerial("SPEC-2023-007")
                .marca("Specialized")
                .tipo(TipoBicicleta.RUTA)
                .color("Rojo")
                .anio(2023)
                .cedulaCliente("1098765432")
                .build();
        bicicletas.add(b1);
        bicicletas.add(b2);

        // Orden de muestra
        String id1 = String.format("ORD-%04d", contadorOrdenes);
        contadorOrdenes++;
        OrdenServicio o1 = new OrdenServicio.Builder()
                .idOrden(id1)
                .fechaIngreso(LocalDate.now())
                .serialBicicleta("TREK-2021-001")
                .codigoMecanico("MEC-001")
                .motivoServicio("Cambio de frenos delanteros")
                .diagnostico("Pastillas desgastadas al 90%")
                .trabajosRealizados("Reemplazo de pastillas y ajuste de cables")
                .costoTotal(85000)
                .estado(EstadoOrden.EN_PROCESO)
                .build();
        ordenes.add(o1);

        // Repuestos de muestra (algunos con stock bajo a propósito para mostrar la alerta)
        Repuesto r1 = new Repuesto.Builder()
                .codigo("REP-001")
                .nombre("Pastillas de freno Shimano")
                .categoria("Frenos")
                .precioUnitario(25000)
                .stockMinimo(5)
                .cantidadDisponible(2)  // stock bajo
                .build();
        Repuesto r2 = new Repuesto.Builder()
                .codigo("REP-002")
                .nombre("Cadena KMC X11")
                .categoria("Transmisión")
                .precioUnitario(75000)
                .stockMinimo(3)
                .cantidadDisponible(8)
                .build();
        Repuesto r3 = new Repuesto.Builder()
                .codigo("REP-003")
                .nombre("Llanta Maxxis 29\"")
                .categoria("Rodado")
                .precioUnitario(120000)
                .stockMinimo(2)
                .cantidadDisponible(1)  // stock bajo
                .build();
        repuestos.add(r1);
        repuestos.add(r2);
        repuestos.add(r3);
    }
}