package taller.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una orden de servicio generada en el taller.
 *
 * <p>Agrupa la información completa de un servicio: bicicleta atendida,
 * mecánico responsable, motivo, diagnóstico, trabajos, costo y el
 * {@link EstadoOrden} actual de la orden (PENDIENTE → EN_PROCESO → COMPLETADA).</p>
 *
 * <p>El número de orden se asigna automáticamente de forma secuencial.</p>
 *
 * @author Simon Valencia
 * @version 1.0
 * @see EstadoOrden
 */
public class OrdenServicio {

    /** Contador estático para la numeración automática de órdenes. */
    private static int contador = 1;

    /** Número único y secuencial de la orden. */
    private final int numeroOrden;

    /** Fecha de ingreso de la bicicleta al taller. */
    private LocalDate fechaIngreso;

    /** Hora de ingreso al taller. */
    private LocalTime horaIngreso;

    /** Bicicleta sobre la que se ejecuta el servicio. */
    private final Bicicleta bicicleta;

    /** Mecánico responsable del servicio. */
    private final Mecanico mecanico;

    /** Motivo o razón declarada por el cliente. */
    private String motivo;

    /** Diagnóstico técnico del mecánico. */
    private String diagnostico;

    /** Descripción de los trabajos ejecutados. */
    private String trabajosRealizados;

    /** Costo total del servicio (≥ 0). */
    private double costoTotal;

    /**
     * Estado actual de la orden dentro del ciclo de vida del servicio.
     * Inicia como {@link EstadoOrden#PENDIENTE} al crearse.
     */
    private EstadoOrden estado;

    // ── Constructor ────────────────────────────────────────────────────────────

    /**
     * Crea una orden de servicio, asigna un número secuencial y la marca
     * como {@link EstadoOrden#PENDIENTE}.
     *
     * @param fechaIngreso       Fecha de ingreso (no nula, no puede ser futura).
     * @param horaIngreso        Hora de ingreso (no nula).
     * @param bicicleta          Bicicleta atendida (no nula).
     * @param mecanico           Mecánico responsable (no nulo).
     * @param motivo             Motivo del servicio (no nulo, no vacío).
     * @param diagnostico        Diagnóstico técnico (no nulo, no vacío).
     * @param trabajosRealizados Trabajos efectuados (no nulo, no vacío).
     * @param costoTotal         Costo total (no negativo).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public OrdenServicio(LocalDate fechaIngreso, LocalTime horaIngreso,
                         Bicicleta bicicleta, Mecanico mecanico,
                         String motivo, String diagnostico,
                         String trabajosRealizados, double costoTotal) {

        Validaciones.noNulo(fechaIngreso, "La fecha de ingreso");
        if (fechaIngreso.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de ingreso no puede ser futura.");
        Validaciones.noNulo(horaIngreso,          "La hora de ingreso");
        Validaciones.noNulo(bicicleta,             "La bicicleta de la orden");
        Validaciones.noNulo(mecanico,              "El mecánico de la orden");
        Validaciones.requerido(motivo,             "El motivo de la orden");
        Validaciones.requerido(diagnostico,        "El diagnóstico de la orden");
        Validaciones.requerido(trabajosRealizados, "Los trabajos realizados");
        Validaciones.costo(costoTotal);

        this.numeroOrden        = contador++;
        this.fechaIngreso       = fechaIngreso;
        this.horaIngreso        = horaIngreso;
        this.bicicleta          = bicicleta;
        this.mecanico           = mecanico;
        this.motivo             = motivo.trim();
        this.diagnostico        = diagnostico.trim();
        this.trabajosRealizados = trabajosRealizados.trim();
        this.costoTotal         = costoTotal;
        this.estado             = EstadoOrden.PENDIENTE;    // estado inicial
    }

    // ── Control del contador ───────────────────────────────────────────────────

    /**
     * Reinicia el contador de órdenes a 1.
     * <b>Solo usar en pruebas unitarias.</b>
     */
    public static void resetContador() { contador = 1; }

    // ── Getters ────────────────────────────────────────────────────────────────

    /** @return número secuencial único de la orden. */
    public int         getNumeroOrden()        { return numeroOrden; }

    /** @return fecha de ingreso de la bicicleta. */
    public LocalDate   getFechaIngreso()       { return fechaIngreso; }

    /** @return hora de ingreso de la bicicleta. */
    public LocalTime   getHoraIngreso()        { return horaIngreso; }

    /** @return bicicleta atendida en esta orden. */
    public Bicicleta   getBicicleta()          { return bicicleta; }

    /** @return mecánico responsable de esta orden. */
    public Mecanico    getMecanico()           { return mecanico; }

    /** @return motivo del servicio. */
    public String      getMotivo()             { return motivo; }

    /** @return diagnóstico técnico. */
    public String      getDiagnostico()        { return diagnostico; }

    /** @return trabajos realizados. */
    public String      getTrabajosRealizados() { return trabajosRealizados; }

    /** @return costo total del servicio. */
    public double      getCostoTotal()         { return costoTotal; }

    /** @return estado actual de la orden ({@link EstadoOrden}). */
    public EstadoOrden getEstado()             { return estado; }

    // ── Setters ────────────────────────────────────────────────────────────────

    /** @param diagnostico Nuevo diagnóstico (no nulo, no vacío). */
    public void setDiagnostico(String diagnostico) {
        Validaciones.requerido(diagnostico, "El diagnóstico");
        this.diagnostico = diagnostico.trim();
    }

    /** @param trabajosRealizados Nuevos trabajos (no nulo, no vacío). */
    public void setTrabajosRealizados(String trabajosRealizados) {
        Validaciones.requerido(trabajosRealizados, "Los trabajos realizados");
        this.trabajosRealizados = trabajosRealizados.trim();
    }

    /** @param costoTotal Nuevo costo (no negativo). */
    public void setCostoTotal(double costoTotal) {
        Validaciones.costo(costoTotal);
        this.costoTotal = costoTotal;
    }

    /**
     * Actualiza el estado de la orden.
     *
     * @param estado Nuevo estado (no nulo).
     * @throws IllegalArgumentException si el estado es nulo.
     */
    public void setEstado(EstadoOrden estado) {
        Validaciones.noNulo(estado, "El estado de la orden");
        this.estado = estado;
    }

    /**
     * @return cadena con número, fecha, bicicleta, estado y costo.
     */
    @Override
    public String toString() {
        return String.format("Orden #%d | %s | %s | %s | $%.2f",
                numeroOrden, fechaIngreso, bicicleta.getMarca(),
                estado.getEtiqueta(), costoTotal);
    }
}
