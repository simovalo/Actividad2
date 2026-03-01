package com.tallerbici;

import com.tallerbici.model.*;
import com.tallerbici.model.enums.EstadoOrden;
import com.tallerbici.model.enums.TipoBicicleta;
import com.tallerbici.model.singleton.GestorTaller;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración: flujo completo de negocio del taller.
 *
 * @author Equipo TallerBici - Programación II
 */
@DisplayName("🔄 Tests de Lógica de Negocio")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LogicaNegocioTest {

    private static final GestorTaller gestor = GestorTaller.getInstance();

    // Datos únicos para estos tests (prefijo INTG para no chocar con datos de muestra)
    private static final String CEDULA_CLIENTE  = "INTG-CC-001";
    private static final String SERIAL_BICI     = "INTG-BICI-001";
    private static final String CODIGO_MECANICO = "INTG-MEC-001";

    @BeforeAll
    static void configurarDatos() {
        // Registrar cliente de prueba solo si no existe
        if (!gestor.existeCliente(CEDULA_CLIENTE)) {
            Cliente cliente = new Cliente.Builder()
                    .numeroCedula(CEDULA_CLIENTE)
                    .nombreCompleto("Cliente de Integración")
                    .telefono("3009999999")
                    .direccion("Calle de Test 456")
                    .build();
            gestor.registrarCliente(cliente);
        }

        // Registrar mecánico de prueba solo si no existe
        // buscarMecanico() devuelve Mecanico o null (no Optional)
        if (gestor.buscarMecanico(CODIGO_MECANICO) == null) {
            Mecanico mec = new Mecanico.Builder()
                    .codigoInterno(CODIGO_MECANICO)
                    .nombreCompleto("Mecánico Integración")
                    .especialidad("General")
                    .numeroCertificacion("CERT-INTG-001")
                    .build();
            gestor.registrarMecanico(mec);
        }

        // Registrar bicicleta de prueba solo si no existe
        // buscarBicicleta() devuelve Bicicleta o null (no Optional)
        if (gestor.buscarBicicleta(SERIAL_BICI) == null) {
            Bicicleta bici = new Bicicleta.Builder()
                    .numeroSerial(SERIAL_BICI)
                    .marca("Bici Integración")
                    .tipo(TipoBicicleta.HIBRIDA)
                    .color("Gris")
                    .anio(2023)
                    .cedulaCliente(CEDULA_CLIENTE)
                    .build();
            gestor.registrarBicicleta(bici);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Flujo 1: Verificar datos de integración creados")
    void testDatosDeIntegracionExisten() {
        assertTrue(gestor.existeCliente(CEDULA_CLIENTE));
        // Verificamos con != null en lugar de .isPresent() (no hay Optional)
        assertNotNull(gestor.buscarMecanico(CODIGO_MECANICO));
        assertNotNull(gestor.buscarBicicleta(SERIAL_BICI));
    }

    @Test
    @Order(2)
    @DisplayName("Flujo 2: Crear una orden de servicio completa")
    void testFlujoCrearOrden() {
        OrdenServicio orden = gestor.crearOrden(
                SERIAL_BICI,
                CODIGO_MECANICO,
                "Revisión general de integración"
        );

        assertNotNull(orden);
        assertEquals(SERIAL_BICI, orden.getSerialBicicleta());
        assertEquals(CODIGO_MECANICO, orden.getCodigoMecanico());
        assertEquals(EstadoOrden.INGRESADA, orden.getEstado());
        assertEquals(LocalDate.now(), orden.getFechaIngreso());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo 3: Ver historial de bicicleta con al menos 1 orden")
    void testHistorialConOrden() {
        ArrayList<OrdenServicio> historial = gestor.getHistorialBicicleta(SERIAL_BICI);

        assertNotNull(historial);
        assertFalse(historial.isEmpty(),
                "El historial debe tener al menos 1 orden (creada en el test anterior)");

        // Verificar con for que todas las órdenes son de la misma bicicleta
        for (int i = 0; i < historial.size(); i++) {
            assertEquals(SERIAL_BICI, historial.get(i).getSerialBicicleta(),
                    "Todas las órdenes deben ser de la bicicleta de integración");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Flujo 4: Actualizar estado de una orden (ciclo de vida)")
    void testCicloVidaOrden() {
        OrdenServicio orden = gestor.crearOrden(
                SERIAL_BICI,
                CODIGO_MECANICO,
                "Test ciclo de vida"
        );

        // INGRESADA → EN_PROCESO → LISTA → ENTREGADA
        assertEquals(EstadoOrden.INGRESADA, orden.getEstado());

        orden.setEstado(EstadoOrden.EN_PROCESO);
        assertEquals(EstadoOrden.EN_PROCESO, orden.getEstado());

        orden.setDiagnostico("Transmisión desgastada");
        orden.setTrabajosRealizados("Cambio de cadena y piñones");
        orden.setCostoTotal(180000);
        orden.setEstado(EstadoOrden.LISTA);
        assertEquals(EstadoOrden.LISTA, orden.getEstado());
        assertEquals(180000, orden.getCostoTotal(), 0.01);

        orden.setEstado(EstadoOrden.ENTREGADA);
        assertEquals(EstadoOrden.ENTREGADA, orden.getEstado());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo 5: Gestión de stock de repuestos")
    void testFlujoStockRepuestos() {
        String codigoRepuesto = "REP-INTG-01";

        // buscarRepuesto() devuelve Repuesto o null (no Optional)
        if (gestor.buscarRepuesto(codigoRepuesto) == null) {
            Repuesto repuesto = new Repuesto.Builder()
                    .codigo(codigoRepuesto)
                    .nombre("Cable cambios integración")
                    .categoria("Transmisión")
                    .precioUnitario(15000)
                    .cantidadDisponible(10)
                    .stockMinimo(3)
                    .build();
            gestor.registrarRepuesto(repuesto);
        }

        // Obtener el repuesto directamente (no .get(), solo buscarRepuesto())
        Repuesto rep = gestor.buscarRepuesto(codigoRepuesto);
        assertNotNull(rep, "El repuesto debe existir");

        int stockInicial = rep.getCantidadDisponible();

        // Usar 8 unidades
        rep.usarStock(8);
        assertEquals(stockInicial - 8, rep.getCantidadDisponible());

        // Ahora tiene stock bajo (10 - 8 = 2, mínimo es 3)
        assertTrue(rep.tieneStockBajo(), "Debe detectar stock bajo después de usar 8 de 10");

        // Reponer stock
        rep.agregarStock(15);
        assertFalse(rep.tieneStockBajo(), "Después de reponer no debe tener stock bajo");
    }

    @Test
    @Order(6)
    @DisplayName("Flujo 6: Búsqueda de bicicletas de un cliente específico")
    void testBicicletasPorCliente() {
        ArrayList<Bicicleta> bicicletas = gestor.getBicicletasDeCliente(CEDULA_CLIENTE);
        assertNotNull(bicicletas);
        assertFalse(bicicletas.isEmpty(), "El cliente debe tener al menos 1 bicicleta");

        // Verificar con for que todas pertenecen al cliente
        for (int i = 0; i < bicicletas.size(); i++) {
            assertEquals(CEDULA_CLIENTE, bicicletas.get(i).getCedulaCliente(),
                    "Todas las bicicletas deben pertenecer al cliente de integración");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Flujo 7: Consulta de órdenes por fecha de hoy")
    void testOrdenesPorFechaHoy() {
        ArrayList<OrdenServicio> ordenesDia = gestor.getOrdenesPorFecha(LocalDate.now());
        assertNotNull(ordenesDia);
        assertFalse(ordenesDia.isEmpty(),
                "Deben existir órdenes de hoy (creadas en tests anteriores)");

        // Verificar con for que todas son de hoy
        for (int i = 0; i < ordenesDia.size(); i++) {
            assertEquals(LocalDate.now(), ordenesDia.get(i).getFechaIngreso(),
                    "Todas las órdenes deben ser de hoy");
        }
    }
}