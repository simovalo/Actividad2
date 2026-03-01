package com.tallerbici.model;

import com.tallerbici.model.enums.EstadoOrden;
import com.tallerbici.model.interfaces.IRegistrable;
import com.tallerbici.model.interfaces.IConsultable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una orden de servicio en el taller.
 * Es el documento principal del sistema: une bicicleta, mecánico y trabajo realizado.
 * Principio S (SRP): maneja solo datos de la orden.
 * Usa el patrón Builder para su construcción.
 *
 * @author Equipo TallerBici - Programación II
 */
public class OrdenServicio implements IRegistrable, IConsultable {

    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA  = DateTimeFormatter.ofPattern("HH:mm");

    private final String idOrden;
    private final LocalDate fechaIngreso;
    private final LocalTime horaIngreso;
    private final String serialBicicleta;
    private final String codigoMecanico;
    private final String motivoServicio;
    private String diagnostico;
    private String trabajosRealizados;
    private double costoTotal;
    private EstadoOrden estado;

    private OrdenServicio(Builder builder) {
        this.idOrden           = builder.idOrden;
        this.fechaIngreso      = builder.fechaIngreso;
        this.horaIngreso       = builder.horaIngreso;
        this.serialBicicleta   = builder.serialBicicleta;
        this.codigoMecanico    = builder.codigoMecanico;
        this.motivoServicio    = builder.motivoServicio;
        this.diagnostico       = builder.diagnostico;
        this.trabajosRealizados= builder.trabajosRealizados;
        this.costoTotal        = builder.costoTotal;
        this.estado            = builder.estado;
    }

    // ======================== GETTERS ========================

    public String getIdOrden()            { return idOrden; }
    public LocalDate getFechaIngreso()    { return fechaIngreso; }
    public LocalTime getHoraIngreso()     { return horaIngreso; }
    public String getSerialBicicleta()    { return serialBicicleta; }
    public String getCodigoMecanico()     { return codigoMecanico; }
    public String getMotivoServicio()     { return motivoServicio; }
    public String getDiagnostico()        { return diagnostico; }
    public String getTrabajosRealizados() { return trabajosRealizados; }
    public double getCostoTotal()         { return costoTotal; }
    public EstadoOrden getEstado()        { return estado; }

    // ======================== SETTERS (campos mutables) ========================

    public void setDiagnostico(String diagnostico)             { this.diagnostico = diagnostico; }
    public void setTrabajosRealizados(String trabajos)         { this.trabajosRealizados = trabajos; }
    public void setCostoTotal(double costoTotal)               { this.costoTotal = costoTotal; }
    public void setEstado(EstadoOrden estado)                  { this.estado = estado; }

    // =================== IRegistrable =======================

    @Override
    public String getId() { return idOrden; }

    @Override
    public String getResumen() {
        return "Orden #" + idOrden + " | " + fechaIngreso.format(FMT_FECHA)
                + " | Serial: " + serialBicicleta + " | " + estado;
    }

    // =================== IConsultable =======================

    @Override
    public String getDetalles() {
        return "=== ORDEN DE SERVICIO #" + idOrden + " ===" +
                "\nFecha: " + fechaIngreso.format(FMT_FECHA) +
                "\nHora: " + horaIngreso.format(FMT_HORA) +
                "\nBicicleta (serial): " + serialBicicleta +
                "\nMecánico (cód): " + codigoMecanico +
                "\nMotivo: " + motivoServicio +
                "\nDiagnóstico: " + (diagnostico != null ? diagnostico : "Pendiente") +
                "\nTrabajos: " + (trabajosRealizados != null ? trabajosRealizados : "Pendiente") +
                "\nCosto total: $" + String.format("%.2f", costoTotal) +
                "\nEstado: " + estado;
    }

    @Override
    public boolean coincideCon(String criterio) {
        String c = criterio.toLowerCase();
        return idOrden.toLowerCase().contains(c)
                || serialBicicleta.toLowerCase().contains(c)
                || codigoMecanico.toLowerCase().contains(c)
                || motivoServicio.toLowerCase().contains(c);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // ====================== BUILDER =========================

    /**
     * Builder para construir una OrdenServicio.
     */
    public static class Builder {

        private String idOrden;
        private LocalDate fechaIngreso      = LocalDate.now();
        private LocalTime horaIngreso       = LocalTime.now();
        private String serialBicicleta;
        private String codigoMecanico;
        private String motivoServicio;
        private String diagnostico          = "";
        private String trabajosRealizados   = "";
        private double costoTotal           = 0.0;
        private EstadoOrden estado          = EstadoOrden.INGRESADA;

        public Builder idOrden(String idOrden) {
            this.idOrden = idOrden;
            return this;
        }

        public Builder fechaIngreso(LocalDate fechaIngreso) {
            this.fechaIngreso = fechaIngreso;
            return this;
        }

        public Builder horaIngreso(LocalTime horaIngreso) {
            this.horaIngreso = horaIngreso;
            return this;
        }

        public Builder serialBicicleta(String serialBicicleta) {
            this.serialBicicleta = serialBicicleta;
            return this;
        }

        public Builder codigoMecanico(String codigoMecanico) {
            this.codigoMecanico = codigoMecanico;
            return this;
        }

        public Builder motivoServicio(String motivoServicio) {
            this.motivoServicio = motivoServicio;
            return this;
        }

        public Builder diagnostico(String diagnostico) {
            this.diagnostico = diagnostico;
            return this;
        }

        public Builder trabajosRealizados(String trabajosRealizados) {
            this.trabajosRealizados = trabajosRealizados;
            return this;
        }

        public Builder costoTotal(double costoTotal) {
            this.costoTotal = costoTotal;
            return this;
        }

        public Builder estado(EstadoOrden estado) {
            this.estado = estado;
            return this;
        }

        public OrdenServicio build() {
            if (idOrden == null || idOrden.isBlank())
                throw new IllegalStateException("El ID de la orden es obligatorio.");
            if (serialBicicleta == null || serialBicicleta.isBlank())
                throw new IllegalStateException("El serial de la bicicleta es obligatorio.");
            if (codigoMecanico == null || codigoMecanico.isBlank())
                throw new IllegalStateException("El código del mecánico es obligatorio.");
            if (motivoServicio == null || motivoServicio.isBlank())
                throw new IllegalStateException("El motivo del servicio es obligatorio.");
            return new OrdenServicio(this);
        }
    }
}