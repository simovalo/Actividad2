package tallerBiciletasTest;

import taller.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TallerBicicletas, Validaciones,
 * la interfaz Identificable, la clase abstracta Persona
 * y el enum EstadoOrden.
 *
 * <p>cubrimos las 7 funcionalidades del sistema más los otros elementos
 * de diseño orientado a objetos.</p>
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

    //  Taller — datos propios

    @Test @Order(1)
    void taller_datosIniciales_sonCorrectos() {
        assertEquals("Ciclo El Parche", taller.getNombre());
        assertEquals("Cra 15 # 82",     taller.getDireccion());
        assertEquals("3001234567",       taller.getTelefono());
    }

    //  Interfaz Identificable — polimorfismo con Cliente y Mecanico

    @Test @Order(2)
    void identificable_tratadoPolimorfico_comoPersona() {
        // Tanto Cliente como Mecanico pueden tratarse como Persona
        Persona c = new Cliente("Ana Gómez", "C01", "3101234567", "Av. 2");
        Persona m = new Mecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertEquals("Ana Gómez",   c.getNombre());
        assertEquals("Carlos López",m.getNombre());
        assertEquals("3101234567",  c.getTelefono());
        assertEquals("3209876543",  m.getTelefono());
    }

    //  Herencia — Persona a Cliente y Mecanico


    @Test @Order(3)
    void herencia_mecanico_esInstanciaDePersona() {
        Mecanico m = new Mecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertInstanceOf(Persona.class, m);
        assertInstanceOf(Identificable.class, m);
    }

    //  Enum EstadoOrden

    @Test @Order(4)
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




    //  Enum Bicicleta.Tipo

    @Test @Order(5)
    void bicicletaTipo_valores_existen() {
        assertNotNull(Bicicleta.Tipo.valueOf("RUTA"));
        assertNotNull(Bicicleta.Tipo.valueOf("MTB"));
        assertNotNull(Bicicleta.Tipo.valueOf("URBANA"));
        assertNotNull(Bicicleta.Tipo.valueOf("ELECTRICA"));
        assertNotNull(Bicicleta.Tipo.valueOf("OTRA"));
    }

    //  Clientes

    @Test @Order(6)
    void registrarCliente_IDDuplicado_lanzaExcepcion() {
        taller.registrarCliente("Juan", "123", "3001234567", "Calle 1");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarCliente("Otro", "123", "3009999999", "Calle 2"));
    }


    // Bicicletas



    @Test @Order(7)
    void registrarBicicleta_SerialDuplicado_lanzaExcepcion() {
        taller.registrarCliente("Ana", "C01", "3101234567", "Av. 2");
        taller.registrarBicicleta("B001", "Trek", Bicicleta.Tipo.MTB, "Azul", 2022, "C01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarBicicleta("B001", "Giant",
                        Bicicleta.Tipo.RUTA, "Rojo", 2021, "C01"));
    }


    //  Mecánicos


    @Test @Order(8)
    void registrarMecanico_CodigoDuplicado_lanzaExcepcion() {
        taller.registrarMecanico("Carlos López", "3209876543", "Frenos", "MEC01");
        assertThrows(IllegalArgumentException.class,
                () -> taller.registrarMecanico("Otro Nombre", "3201111111", "Suspensión", "MEC01"));
    }

    //   Órdenes

    @Test @Order(9)
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



    //   Historial bicicleta

    @Test @Order(10)
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

    //   Órdenes por fecha

    @Test @Order(11)
    void ordenesPorFecha_fechaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> taller.ordenesPorFecha(null));
    }

    //  Costos acumulados por cliente

    @Test @Order(12)
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


    //  Colecciones inmutables

    @Test @Order(13)
    void getClientes_retornaVistaInmutable() {
        taller.registrarCliente("Ana","C01","3101234567","Av. 2");
        assertThrows(UnsupportedOperationException.class,
                () -> taller.getClientes().clear());
    }

}
