package com.tallerbici;

import com.tallerbici.model.Cliente;
import com.tallerbici.model.Mecanico;
import com.tallerbici.model.Bicicleta;
import com.tallerbici.model.OrdenServicio;
import com.tallerbici.model.enums.TipoBicicleta;
import com.tallerbici.model.singleton.GestorTaller;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del patrón Singleton y las operaciones del GestorTaller.
 *
 * @author Equipo TallerBici - Programación II
 */
@DisplayName("🔒 Tests del Singleton GestorTaller")
class GestorTallerTest {

    private GestorTaller gestor;

    @BeforeEach
    void setUp() {
        gestor = GestorTaller.getInstance();
    }

    // ====================== SINGLETON =======================

    @Test
    @DisplayName("Singleton: getInstance() siempre retorna la misma instancia")
    void testSingletonMismaInstancia() {
        GestorTaller instancia1 = GestorTaller.getInstance();
        GestorTaller instancia2 = GestorTaller.getInstance();

        // Las dos referencias deben apuntar exactamente al mismo objeto
        assertSame(instancia1, instancia2,
                "El Singleton debe retornar siempre la misma instancia");
    }

    @Test
    @DisplayName("Singleton: no se puede crear con 'new' (constructor privado)")
    void testSingletonConstructorPrivado() {
        try {
            var constructor = GestorTaller.class.getDeclaredConstructor();
            assertTrue(
                    java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
                    "El constructor del Singleton debe ser privado"
            );
        } catch (NoSuchMethodException e) {
            fail("No se encontró el constructor del GestorTaller");
        }
    }

    // ====================== CLIENTES =======================

    @Test
    @DisplayName("Registrar un cliente nuevo correctamente")
    void testRegistrarClienteNuevo() {
        String cedulaTest = "9991112222";

        if (!gestor.existeCliente(cedulaTest)) {
            Cliente clienteTest = new Cliente.Builder()
                    .numeroCedula(cedulaTest)
                    .nombreCompleto("Test Usuario")
                    .telefono("3199998888")
                    .direccion("Calle de prueba 123")
                    .build();
            gestor.registrarCliente(clienteTest);
        }

        assertTrue(gestor.existeCliente(cedulaTest),
                "El cliente debe existir después de registrarlo");

        // buscarCliente() devuelve el objeto directamente (no Optional)
        Cliente encontrado = gestor.buscarCliente(cedulaTest);
        assertNotNull(encontrado, "buscarCliente debe retornar el cliente");
        assertEquals("Test Usuario", encontrado.getNombreCompleto());
    }

    @Test
    @DisplayName("No se puede registrar dos clientes con la misma cédula")
    void testNoDuplicarCliente() {
        // Los datos de muestra ya tienen cliente con cédula "1012345678"
        Cliente duplicado = new Cliente.Builder()
                .numeroCedula("1012345678")
                .nombreCompleto("Duplicado Test")
                .telefono("3000000000")
                .direccion("Dirección duplicada")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> gestor.registrarCliente(duplicado),
                "Debe lanzar excepción al registrar cédula duplicada");
    }

    @Test
    @DisplayName("La lista de clientes no debe ser null ni vacía")
    void testListaClientesNoNull() {
        // getListaClientes() es el método correcto (reemplaza getTodosLosClientes)
        assertNotNull(gestor.getListaClientes(), "La lista de clientes no puede ser null");
        assertFalse(gestor.getListaClientes().isEmpty(),
                "Deben existir clientes (cargados en datos de muestra)");
    }

    // ====================== BICICLETAS =======================

    @Test
    @DisplayName("No se puede registrar bicicleta con cliente inexistente")
    void testBicicletaClienteInexistente() {
        Bicicleta bici = new Bicicleta.Builder()
                .numeroSerial("TEST-BICI-999")
                .marca("MarcaTest")
                .tipo(TipoBicicleta.URBANA)
                .color("Azul")
                .anio(2022)
                .cedulaCliente("0000000000") // cédula que no existe
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> gestor.registrarBicicleta(bici),
                "Debe lanzar excepción si el cliente dueño no existe");
    }

    @Test
    @DisplayName("Historial de bicicleta devuelve lista")
    void testHistorialBicicleta() {
        ArrayList<OrdenServicio> historial = gestor.getHistorialBicicleta("TREK-2021-001");
        assertNotNull(historial, "El historial no puede ser null");
    }

    // ====================== MECÁNICOS =======================

    @Test
    @DisplayName("Buscar mecánico de los datos de muestra")
    void testBuscarMecanico() {
        // buscarMecanico() devuelve Mecanico directamente (no Optional)
        Mecanico mecanico = gestor.buscarMecanico("MEC-001");
        assertNotNull(mecanico, "Debe existir el mecánico MEC-001 (dato de muestra)");
        assertEquals("Javier Hernández", mecanico.getNombreCompleto());
    }

    // ====================== ÓRDENES =======================

    @Test
    @DisplayName("Crear una orden de servicio válida")
    void testCrearOrdenValida() {
        // getListaOrdenes() es el método correcto (reemplaza getTodasLasOrdenes)
        int cantidadAntes = gestor.getListaOrdenes().size();

        OrdenServicio orden = gestor.crearOrden(
                "TREK-2021-001",
                "MEC-001",
                "Test: ajuste de frenos"
        );

        assertNotNull(orden, "La orden creada no puede ser null");
        assertNotNull(orden.getIdOrden(), "La orden debe tener un ID");
        assertTrue(orden.getIdOrden().startsWith("ORD-"), "El ID debe empezar con 'ORD-'");
        assertEquals(cantidadAntes + 1, gestor.getListaOrdenes().size(),
                "La cantidad de órdenes debe aumentar en 1");
    }

    @Test
    @DisplayName("Crear orden con bicicleta inexistente lanza excepción")
    void testCrearOrdenBicicletaInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> gestor.crearOrden("SERIAL-QUE-NO-EXISTE", "MEC-001", "motivo"),
                "Debe lanzar excepción si la bicicleta no existe");
    }

    @Test
    @DisplayName("Buscar órdenes por fecha devuelve lista")
    void testOrdenesPorFecha() {
        ArrayList<OrdenServicio> hoy = gestor.getOrdenesPorFecha(LocalDate.now());
        assertNotNull(hoy, "La lista de órdenes por fecha no puede ser null");
    }
}