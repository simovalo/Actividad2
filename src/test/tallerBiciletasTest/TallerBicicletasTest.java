package tallerBiciletasTest;

import taller.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link TallerBicicletas}, {@link Validaciones},
 * la interfaz {@link Identificable}, la clase abstracta {@link Persona}
 * y el enum {@link EstadoOrden}.
 *
 * <p>Cubre las 7 funcionalidades del sistema más los nuevos elementos
 * de diseño orientado a objetos incorporados al modelo.</p>
 *
 * @author Juan Manuel Vera
 * @version 2.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TallerBicicletasTest {

    private TallerBicicletas taller;

    @BeforeEach
    void setUp() {
        taller = new TallerBicicletas("Ciclo El Parche", "Cra 15 # 82", "3001234567");
        OrdenServicio.resetContador();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Taller — datos propios
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(1)
    void taller_datosIniciales_sonCorrectos() {
        assertEquals("Ciclo El Parche", taller.getNombre());
        assertEquals("Cra 15 # 82",     taller.getDireccion());
        assertEquals("3001234567",       taller.getTelefono());
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  Interfaz Identificable — polimorfismo con Cliente y Mecanico
    // ══════════════════════════════════════════════════════════════════════════


    @Test @Order(7)
    void identificable_tratadoPolimorfico_comoPersona() {
        // Tanto Cliente como Mecanico pueden tratarse como Persona
        Persona c = new Cliente("Ana Gómez", "C01", "3101234567", "Av. 2");
        Persona m = new Mecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertEquals("Ana Gómez",   c.getNombre());
        assertEquals("Carlos López",m.getNombre());
        assertEquals("3101234567",  c.getTelefono());
        assertEquals("3209876543",  m.getTelefono());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Herencia — Persona → Cliente y Mecanico
    // ══════════════════════════════════════════════════════════════════════════


    @Test @Order(9)
    void herencia_mecanico_esInstanciaDePersona() {
        Mecanico m = new Mecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertInstanceOf(Persona.class, m);
        assertInstanceOf(Identificable.class, m);
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  Enum EstadoOrden
    // ══════════════════════════════════════════════════════════════════════════


    @Test @Order(13)
    void estadoOrden_setEstado_actualizaCorrectamente() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        OrdenServicio o = taller.crearOrden("B001", "MEC01", LocalDate.now(),
                LocalTime.now(), "Revisión", "Frenos", "Ajuste", 50000);
        o.setEstado(EstadoOrden.EN_PROCESO);
        assertEquals(EstadoOrden.EN_PROCESO, o.getEstado());
        o.setEstado(EstadoOrden.COMPLETADA);
        assertEquals(EstadoOrden.COMPLETADA, o.getEstado());
    }

    

    // ══════════════════════════════════════════════════════════════════════════
    //  Enum Bicicleta.Tipo
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(16)
    void bicicletaTipo_valores_existen() {
        assertNotNull(Bicicleta.Tipo.valueOf("RUTA"));
        assertNotNull(Bicicleta.Tipo.valueOf("MTB"));
        assertNotNull(Bicicleta.Tipo.valueOf("URBANA"));
        assertNotNull(Bicicleta.Tipo.valueOf("ELECTRICA"));
        assertNotNull(Bicicleta.Tipo.valueOf("OTRA"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F1 — Clientes
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(17)
    void registrarCliente_OK_aumentaLista() {
        taller.registrarCliente("Juan Pérez", "123", "3001234567", "Calle 1");
        assertEquals(1, taller.getClientes().size());
    }

    @Test @Order(18)
    void registrarCliente_IDDuplicado_lanzaExcepcion() {
        taller.registrarCliente("Juan", "123", "3001234567", "Calle 1");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarCliente("Otro", "123", "3009999999", "Calle 2"));
    }

    @Test @Order(19)
    void registrarCliente_TelefonoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarCliente("Ana", "C01", "abc123", "Dir"));
    }

    @Test @Order(20)
    void buscarCliente_NoExiste_retornaNull() {
        assertNull(taller.buscarCliente("999"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F2 — Bicicletas
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(21)
    void registrarBicicleta_OK_asociaAlCliente() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        Bicicleta b = taller.registrarBicicleta("B001", "Trek",
                Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        assertEquals("C01", b.getPropietario().getIdCliente());
        assertEquals(1, taller.getClientes().get(0).getBicicletas().size());
    }

    @Test @Order(22)
    void registrarBicicleta_AnioFuturo_lanzaExcepcion() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        int anioFuturo = LocalDate.now().getYear() + 1;
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarBicicleta("B001", "Trek",
                        Bicicleta.Tipo.MTB, "Azul", anioFuturo, "C01"));
    }

    @Test @Order(23)
    void registrarBicicleta_SerialDuplicado_lanzaExcepcion() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarBicicleta("B001", "Giant",
                        Bicicleta.Tipo.RUTA, "Rojo", 2021, "C01"));
    }

    @Test @Order(24)
    void registrarBicicleta_ClienteInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarBicicleta("B001", "Trek",
                        Bicicleta.Tipo.URBANA, "Gris", 2020, "NOPE"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F3 — Mecánicos
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(25)
    void registrarMecanico_OK() {
        Mecanico m = taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertEquals("Carlos López", m.getNombre());
        assertEquals("3209876543",   m.getTelefono());   // hereda de Persona
    }

    @Test @Order(26)
    void registrarMecanico_NombreMuyCorto_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarMecanico("Al", "3200000000", "Frenos", "MEC01"));
    }

    @Test @Order(27)
    void registrarMecanico_CodigoDuplicado_lanzaExcepcion() {
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarMecanico("Otro Nombre", "3201111111", "Suspensión", "MEC01"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F4 — Órdenes
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(28)
    void crearOrden_OK_numerosSecuenciales() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        OrdenServicio o1 = taller.crearOrden("B001","MEC01", LocalDate.now(),
                LocalTime.now(),"Mantenimiento","Frenos desgastados","Cambio pastillas",80000);
        OrdenServicio o2 = taller.crearOrden("B001","MEC01", LocalDate.now(),
                LocalTime.now(),"Revisión","OK","Ajuste general",40000);
        assertEquals(1, o1.getNumeroOrden());
        assertEquals(2, o2.getNumeroOrden());
        assertEquals(EstadoOrden.PENDIENTE, o1.getEstado()); // siempre inicia PENDIENTE
    }

    @Test @Order(29)
    void crearOrden_FechaFutura_lanzaExcepcion() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.crearOrden("B001","MEC01", LocalDate.now().plusDays(1),
                        LocalTime.now(),"X","X","X",0));
    }

    @Test @Order(30)
    void crearOrden_CostoNegativo_lanzaExcepcion() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.crearOrden("B001","MEC01", LocalDate.now(),
                        LocalTime.now(),"X","X","X",-500));
    }

    @Test @Order(31)
    void crearOrden_BicicletaInexistente_lanzaExcepcion() {
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.crearOrden("NO_EXISTE","MEC01",
                        LocalDate.now(),LocalTime.now(),"X","X","X",0));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F5 — Historial bicicleta
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(32)
    void historialBicicleta_retornaOrdenesCorrectas() {
        taller.registrarCliente("Ana",  "C01", "3101234567", "Av. 2");
        taller.registrarCliente("Luis", "C02", "3209876543", "Av. 3");
        taller.registrarBicicleta("B001","Trek",  Bicicleta.Tipo.MTB,  "Azul",2022,"C01");
        taller.registrarBicicleta("B002","Giant", Bicicleta.Tipo.RUTA, "Rojo",2021,"C02");
        taller.registrarMecanico("Carlos López","3200000000","Frenos","MEC01");
        taller.crearOrden("B001","MEC01",LocalDate.now(),LocalTime.now(),"M1","D1","T1",50000);
        taller.crearOrden("B001","MEC01",LocalDate.now(),LocalTime.now(),"M2","D2","T2",30000);
        taller.crearOrden("B002","MEC01",LocalDate.now(),LocalTime.now(),"M3","D3","T3",70000);
        assertEquals(2, taller.historialBicicleta("B001").size());
        assertEquals(1, taller.historialBicicleta("B002").size());
    }

    @Test @Order(33)
    void historialBicicleta_SerialInexistente_retornaListaVacia() {
        assertTrue(taller.historialBicicleta("NADA").isEmpty());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F6 — Órdenes por fecha
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(34)
    void ordenesPorFecha_filtroCorrectoPorDia() {
        taller.registrarCliente("Ana","C01","3101234567","Av. 2");
        taller.registrarBicicleta("B001","Trek",Bicicleta.Tipo.MTB,"Azul",2022,"C01");
        taller.registrarMecanico("Carlos López","3200000000","Frenos","MEC01");
        LocalDate hoy  = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);
        taller.crearOrden("B001","MEC01",hoy, LocalTime.now(),"A","A","A",1000);
        taller.crearOrden("B001","MEC01",hoy, LocalTime.now(),"B","B","B",2000);
        taller.crearOrden("B001","MEC01",ayer,LocalTime.now(),"C","C","C",3000);
        assertEquals(2, taller.ordenesPorFecha(hoy).size());
        assertEquals(1, taller.ordenesPorFecha(ayer).size());
        assertEquals(0, taller.ordenesPorFecha(hoy.plusDays(10)).size());
    }

    @Test @Order(35)
    void ordenesPorFecha_fechaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> taller.ordenesPorFecha(null));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  F7 — Costos acumulados por cliente
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(36)
    void costosAcumulados_sumaCorrectamente() {
        taller.registrarCliente("Ana",  "C01","3101234567","Av. 2");
        taller.registrarCliente("Luis", "C02","3209876543","Av. 3");
        taller.registrarBicicleta("B001","Trek",  Bicicleta.Tipo.MTB,  "Azul",2022,"C01");
        taller.registrarBicicleta("B002","Giant", Bicicleta.Tipo.RUTA, "Rojo",2021,"C01");
        taller.registrarBicicleta("B003","Scott", Bicicleta.Tipo.URBANA,"Gris",2020,"C02");
        taller.registrarMecanico("Carlos López","3200000000","Frenos","MEC01");
        taller.crearOrden("B001","MEC01",LocalDate.now(),LocalTime.now(),"M","D","T",50000);
        taller.crearOrden("B002","MEC01",LocalDate.now(),LocalTime.now(),"M","D","T",30000);
        taller.crearOrden("B003","MEC01",LocalDate.now(),LocalTime.now(),"M","D","T",70000);
        assertEquals(80000.0, taller.costosAcumuladosPorCliente("C01"), 0.01);
        assertEquals(70000.0, taller.costosAcumuladosPorCliente("C02"), 0.01);
    }

    @Test @Order(37)
    void costosAcumulados_clienteSinOrdenes_retornaCero() {
        taller.registrarCliente("Ana","C01","3101234567","Av. 2");
        assertEquals(0.0, taller.costosAcumuladosPorCliente("C01"), 0.01);
    }

    @Test @Order(38)
    void costosAcumulados_ClienteInexistente_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> taller.costosAcumuladosPorCliente("NOPE"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Colecciones inmutables
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(39)
    void getClientes_retornaVistaInmutable() {
        taller.registrarCliente("Ana","C01","3101234567","Av. 2");
        assertThrows(UnsupportedOperationException.class,
                () -> taller.getClientes().clear());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  buscarPersona — método genérico polimórfico
    // ══════════════════════════════════════════════════════════════════════════

    @Test @Order(40)
    void buscarPersona_encuentraClientePorId() {
        taller.registrarCliente("Ana","C01","3101234567","Av. 2");
        assertNotNull(taller.buscarCliente("C01"));
        assertNull(taller.buscarCliente("C99"));
    }

    @Test @Order(41)
    void buscarPersona_encuentraMecanicoPorCodigo() {
        taller.registrarMecanico("Carlos López","3200000000","Frenos","MEC01");
        assertNotNull(taller.buscarMecanico("MEC01"));
        assertNull(taller.buscarMecanico("MEC99"));
    }
}
