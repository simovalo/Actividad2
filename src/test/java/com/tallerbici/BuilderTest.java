package com.tallerbici;

import com.tallerbici.model.*;
import com.tallerbici.model.enums.EstadoOrden;
import com.tallerbici.model.enums.TipoBicicleta;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del patrón Builder para todos los modelos.
 * Verifica construcción correcta y validaciones de campos obligatorios.
 *
 * @author Equipo TallerBici - Programación II
 */
@DisplayName("🏗 Tests del patrón Builder")
class BuilderTest {

    // ====================== CLIENTE BUILDER =======================

    @Nested
    @DisplayName("Builder de Cliente")
    class ClienteBuilderTest {

        @Test
        @DisplayName("Construir cliente con todos los datos válidos")
        void testBuildClienteValido() {
            Cliente cliente = new Cliente.Builder()
                    .numeroCedula("1234567890")
                    .nombreCompleto("Ana Sofía Martínez")
                    .telefono("3156789012")
                    .direccion("Av. El Dorado #68B-31, Bogotá")
                    .build();

            assertNotNull(cliente);
            assertEquals("1234567890", cliente.getNumeroCedula());
            assertEquals("Ana Sofía Martínez", cliente.getNombreCompleto());
            assertEquals("3156789012", cliente.getTelefono());
        }

        @Test
        @DisplayName("Builder lanza excepción si falta la cédula")
        void testBuildClienteSinCedula() {
            assertThrows(IllegalStateException.class, () ->
                            new Cliente.Builder()
                                    .nombreCompleto("Sin Cédula Test")
                                    .telefono("3000000000")
                                    .direccion("Alguna dirección")
                                    .build(),
                    "Debe lanzar excepción si falta la cédula"
            );
        }

        @Test
        @DisplayName("Builder lanza excepción si falta el nombre")
        void testBuildClienteSinNombre() {
            assertThrows(IllegalStateException.class, () ->
                    new Cliente.Builder()
                            .numeroCedula("9999999")
                            .telefono("3000000000")
                            .direccion("Alguna dirección")
                            .build()
            );
        }

        @Test
        @DisplayName("getId() retorna la cédula")
        void testClienteGetId() {
            Cliente cliente = new Cliente.Builder()
                    .numeroCedula("111222333")
                    .nombreCompleto("Test getId")
                    .telefono("3000000001")
                    .direccion("Calle 1")
                    .build();
            assertEquals("111222333", cliente.getId());
        }

        @Test
        @DisplayName("coincideCon() funciona correctamente")
        void testClienteCoincideCon() {
            Cliente cliente = new Cliente.Builder()
                    .numeroCedula("555666777")
                    .nombreCompleto("Camilo Restrepo Zapata")
                    .telefono("3101234567")
                    .direccion("Calle 50 Medellín")
                    .build();

            assertTrue(cliente.coincideCon("camilo"), "Debe coincidir con nombre parcial");
            assertTrue(cliente.coincideCon("555666"), "Debe coincidir con cédula parcial");
            assertFalse(cliente.coincideCon("xyz999"), "No debe coincidir con texto inexistente");
        }
    }

    // ====================== BICICLETA BUILDER =======================

    @Nested
    @DisplayName("Builder de Bicicleta")
    class BicicletaBuilderTest {

        @Test
        @DisplayName("Construir bicicleta con todos los datos válidos")
        void testBuildBicicletaValida() {
            Bicicleta bici = new Bicicleta.Builder()
                    .numeroSerial("BICI-TEST-2024")
                    .marca("Trek")
                    .tipo(TipoBicicleta.MTB)
                    .color("Verde")
                    .anio(2024)
                    .cedulaCliente("1234567890")
                    .build();

            assertNotNull(bici);
            assertEquals("BICI-TEST-2024", bici.getNumeroSerial());
            assertEquals("Trek", bici.getMarca());
            assertEquals(TipoBicicleta.MTB, bici.getTipo());
        }

        @Test
        @DisplayName("Builder lanza excepción con año inválido")
        void testBuildBicicletaAnioInvalido() {
            assertThrows(IllegalStateException.class, () ->
                    new Bicicleta.Builder()
                            .numeroSerial("TEST-001")
                            .marca("Marca")
                            .tipo(TipoBicicleta.RUTA)
                            .color("Rojo")
                            .anio(1800) // Año inválido
                            .cedulaCliente("123")
                            .build()
            );
        }

        @Test
        @DisplayName("Builder lanza excepción si falta el serial")
        void testBuildBicicletaSinSerial() {
            assertThrows(IllegalStateException.class, () ->
                    new Bicicleta.Builder()
                            .marca("Trek")
                            .tipo(TipoBicicleta.URBANA)
                            .color("Azul")
                            .anio(2022)
                            .cedulaCliente("123456")
                            .build()
            );
        }
    }

    // ====================== MECANICO BUILDER =======================

    @Nested
    @DisplayName("Builder de Mecánico")
    class MecanicoBuilderTest {

        @Test
        @DisplayName("Construir mecánico válido")
        void testBuildMecanicoValido() {
            Mecanico mec = new Mecanico.Builder()
                    .codigoInterno("MEC-TEST")
                    .nombreCompleto("Luis Pérez")
                    .especialidad("Suspensión")
                    .numeroCertificacion("CERT-TEST-001")
                    .build();

            assertNotNull(mec);
            assertEquals("MEC-TEST", mec.getCodigoInterno());
            assertEquals("MEC-TEST", mec.getId());
        }

        @Test
        @DisplayName("Builder lanza excepción si falta el código")
        void testBuildMecanicoSinCodigo() {
            assertThrows(IllegalStateException.class, () ->
                    new Mecanico.Builder()
                            .nombreCompleto("Sin Código")
                            .especialidad("General")
                            .numeroCertificacion("CERT-000")
                            .build()
            );
        }
    }

    // ====================== REPUESTO BUILDER =======================

    @Nested
    @DisplayName("Builder de Repuesto")
    class RepuestoBuilderTest {

        @Test
        @DisplayName("Construir repuesto válido con stock inicial")
        void testBuildRepuestoValido() {
            Repuesto rep = new Repuesto.Builder()
                    .codigo("REP-TEST-01")
                    .nombre("Cadena de prueba")
                    .categoria("Transmisión")
                    .precioUnitario(50000)
                    .cantidadDisponible(10)
                    .stockMinimo(3)
                    .build();

            assertNotNull(rep);
            assertEquals("REP-TEST-01", rep.getCodigo());
            assertEquals(10, rep.getCantidadDisponible());
            assertFalse(rep.tieneStockBajo(), "Con 10 unidades no debe tener stock bajo");
        }

        @Test
        @DisplayName("Repuesto detecta stock bajo correctamente")
        void testRepuestoStockBajo() {
            Repuesto rep = new Repuesto.Builder()
                    .codigo("REP-TEST-02")
                    .nombre("Repuesto stock bajo")
                    .categoria("Frenos")
                    .precioUnitario(25000)
                    .cantidadDisponible(2) // Por debajo del mínimo default (3)
                    .stockMinimo(3)
                    .build();

            assertTrue(rep.tieneStockBajo(), "Con 2 unidades y mínimo 3 debe detectar stock bajo");
        }

        @Test
        @DisplayName("agregarStock aumenta la cantidad disponible")
        void testAgregarStock() {
            Repuesto rep = new Repuesto.Builder()
                    .codigo("REP-TEST-03")
                    .nombre("Repuesto reposición")
                    .categoria("General")
                    .precioUnitario(10000)
                    .cantidadDisponible(1)
                    .stockMinimo(3)
                    .build();

            rep.agregarStock(5);
            assertEquals(6, rep.getCantidadDisponible(),
                    "Después de agregar 5, debe tener 6 unidades");
        }

        @Test
        @DisplayName("usarStock lanza excepción si no hay suficiente")
        void testUsarStockInsuficiente() {
            Repuesto rep = new Repuesto.Builder()
                    .codigo("REP-TEST-04")
                    .nombre("Repuesto escaso")
                    .categoria("General")
                    .precioUnitario(10000)
                    .cantidadDisponible(2)
                    .build();

            assertThrows(IllegalStateException.class,
                    () -> rep.usarStock(5),
                    "Debe lanzar excepción si se intenta usar más stock del disponible");
        }
    }

    // ====================== ORDEN DE SERVICIO BUILDER =======================

    @Nested
    @DisplayName("Builder de OrdenServicio")
    class OrdenServicioBuilderTest {

        @Test
        @DisplayName("Construir orden de servicio completa")
        void testBuildOrdenCompleta() {
            OrdenServicio orden = new OrdenServicio.Builder()
                    .idOrden("ORD-TEST")
                    .serialBicicleta("SERIAL-001")
                    .codigoMecanico("MEC-001")
                    .motivoServicio("Revisión general de prueba")
                    .diagnostico("Sin fallas mayores")
                    .trabajosRealizados("Limpieza y lubricación")
                    .costoTotal(45000)
                    .estado(EstadoOrden.LISTA)
                    .build();

            assertNotNull(orden);
            assertEquals("ORD-TEST", orden.getIdOrden());
            assertEquals(EstadoOrden.LISTA, orden.getEstado());
            assertEquals(45000, orden.getCostoTotal(), 0.01);
        }

        @Test
        @DisplayName("Estado por defecto es INGRESADA")
        void testOrdenEstadoDefault() {
            OrdenServicio orden = new OrdenServicio.Builder()
                    .idOrden("ORD-DEFAULT")
                    .serialBicicleta("SERIAL-001")
                    .codigoMecanico("MEC-001")
                    .motivoServicio("Prueba estado default")
                    .build();

            assertEquals(EstadoOrden.INGRESADA, orden.getEstado(),
                    "El estado por defecto debe ser INGRESADA");
        }

        @Test
        @DisplayName("Fecha por defecto es hoy")
        void testOrdenFechaDefault() {
            OrdenServicio orden = new OrdenServicio.Builder()
                    .idOrden("ORD-FECHA")
                    .serialBicicleta("SERIAL-001")
                    .codigoMecanico("MEC-001")
                    .motivoServicio("Prueba fecha default")
                    .build();

            assertEquals(LocalDate.now(), orden.getFechaIngreso(),
                    "La fecha por defecto debe ser la de hoy");
        }

        @Test
        @DisplayName("Se puede actualizar el estado de una orden")
        void testActualizarEstadoOrden() {
            OrdenServicio orden = new OrdenServicio.Builder()
                    .idOrden("ORD-ESTADO")
                    .serialBicicleta("SERIAL-001")
                    .codigoMecanico("MEC-001")
                    .motivoServicio("Test actualizar estado")
                    .build();

            orden.setEstado(EstadoOrden.EN_PROCESO);
            assertEquals(EstadoOrden.EN_PROCESO, orden.getEstado());

            orden.setEstado(EstadoOrden.ENTREGADA);
            assertEquals(EstadoOrden.ENTREGADA, orden.getEstado());
        }
    }
}