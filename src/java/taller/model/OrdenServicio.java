package taller.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * representa una orden de servicio generada.
 *
 * <p>agrupa la información completa de un servicio: bicicleta atendida,
 * mecánico responsable, motivo, diagnóstico, trabajos, costo y el
 * EstadoOrden actual de la orden (PENDIENTE > EN_PROCESO > COMPLETADA).</p>
 *
 * <p>el número de orden se asigna automáticamente de forma secuencial.</p>
 *
 * @author Simon Valencia
 * @version 1.0
 * @see EstadoOrden
 */
public class OrdenServicio {

    private static int contador = 1;
    private final int numeroOrden;
    private LocalDate fechaIngreso;
    private LocalTime horaIngreso;
    private final Bicicleta bicicleta;
    private final Mecanico mecanico;
    private String motivo;
    private String diagnostico;
    private String trabajosRealizados;
    private double costoTotal;
    private List<TallerBicicletas> ownedByTallerBicicletas;

    /**
     * estado actual de la orden.
     * Inicia como EstadoOrden#PENDIENTE al crearse.
     */
    private EstadoOrden estado;

    // Constructor

    /**
     * crea una orden de servicio, asigna un número secuencial y la marca
     * como EstadoOrden#PENDIENTE.
     *
     * @param fechaIngreso       fecha de ingreso (no nula, no puede ser futura).
     * @param horaIngreso        hora de ingreso (no nula).
     * @param bicicleta          bicicleta atendida (no nula).
     * @param mecanico           mecánico responsable (no nulo).
     * @param motivo             motivo del servicio (no nulo, no vacío).
     * @param diagnostico        diagnóstico técnico (no nulo, no vacío).
     * @param trabajosRealizados trabajos efectuados (no nulo, no vacío).
     * @param costoTotal         costo total (no negativo).
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
        this.ownedByTallerBicicletas = List.of();
    }

    // ── Control del contador ───────────────────────────────────────────────────

    /**
     * reinicia el contador de órdenes a 1.
     * <b>solo se usa en pruebas unitarias.</b>
     */
    public static void resetContador() { contador = 1; }

    // Getters

    public int         getNumeroOrden()        { return numeroOrden; }
    public LocalDate   getFechaIngreso()       { return fechaIngreso; }
    public LocalTime   getHoraIngreso()        { return horaIngreso; }
    public Bicicleta   getBicicleta()          { return bicicleta; }
    public Mecanico    getMecanico()           { return mecanico; }
    public String      getMotivo()             { return motivo; }
    public String      getDiagnostico()        { return diagnostico; }
    public String      getTrabajosRealizados() { return trabajosRealizados; }
    public double      getCostoTotal()         { return costoTotal; }
    public EstadoOrden getEstado()             { return estado; }
    public List<TallerBicicletas> getOwnedByTallerBicicletas() { return ownedByTallerBicicletas; }

    // Setters

    /** @param diagnostico nuevo diagnóstico (no nulo, no vacío). */
    public void setDiagnostico(String diagnostico) {
        Validaciones.requerido(diagnostico, "El diagnóstico");
        this.diagnostico = diagnostico.trim();
    }

    /** @param trabajosRealizados nuevo trabajos (no nulo, no vacío). */
    public void setTrabajosRealizados(String trabajosRealizados) {
        Validaciones.requerido(trabajosRealizados, "Los trabajos realizados");
        this.trabajosRealizados = trabajosRealizados.trim();
    }

    /** @param costoTotal nuevo costo (no negativo). */
    public void setCostoTotal(double costoTotal) {
        Validaciones.costo(costoTotal);
        this.costoTotal = costoTotal;
    }

    /**
     * Actualiza el estado de la orden.
     *
     * @param estado nuevo estado (no nulo).
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
