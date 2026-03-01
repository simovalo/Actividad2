package com.tallerbici.model;

import com.tallerbici.model.interfaces.IRegistrable;
import com.tallerbici.model.interfaces.IConsultable;

/**
 * Representa un mecánico del taller de bicicletas.
 * Principio S (SRP): solo maneja datos del mecánico.
 * Usa el patrón Builder para su construcción.
 *
 * @author Equipo TallerBici - Programación II
 */
public class Mecanico implements IRegistrable, IConsultable {

    private final String codigoInterno;
    private final String nombreCompleto;
    private final String especialidad;
    private final String numeroCertificacion;

    private Mecanico(Builder builder) {
        this.codigoInterno       = builder.codigoInterno;
        this.nombreCompleto      = builder.nombreCompleto;
        this.especialidad        = builder.especialidad;
        this.numeroCertificacion = builder.numeroCertificacion;
    }

    // ======================== GETTERS ========================

    public String getCodigoInterno()       { return codigoInterno; }
    public String getNombreCompleto()      { return nombreCompleto; }
    public String getEspecialidad()        { return especialidad; }
    public String getNumeroCertificacion() { return numeroCertificacion; }

    // =================== IRegistrable =======================

    @Override
    public String getId() {
        return codigoInterno;
    }

    @Override
    public String getResumen() {
        return nombreCompleto + " - " + especialidad + " (Cód: " + codigoInterno + ")";
    }

    // =================== IConsultable =======================

    @Override
    public String getDetalles() {
        return "Mecánico: " + nombreCompleto +
                "\nCódigo: " + codigoInterno +
                "\nEspecialidad: " + especialidad +
                "\nCertificación: " + numeroCertificacion;
    }

    @Override
    public boolean coincideCon(String criterio) {
        String c = criterio.toLowerCase();
        return nombreCompleto.toLowerCase().contains(c)
                || codigoInterno.toLowerCase().contains(c)
                || especialidad.toLowerCase().contains(c);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // ====================== BUILDER =========================

    /**
     * Builder para construir un Mecánico de forma segura.
     */
    public static class Builder {

        private String codigoInterno;
        private String nombreCompleto;
        private String especialidad;
        private String numeroCertificacion;

        public Builder codigoInterno(String codigoInterno) {
            this.codigoInterno = codigoInterno;
            return this;
        }

        public Builder nombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
            return this;
        }

        public Builder especialidad(String especialidad) {
            this.especialidad = especialidad;
            return this;
        }

        public Builder numeroCertificacion(String numeroCertificacion) {
            this.numeroCertificacion = numeroCertificacion;
            return this;
        }

        public Mecanico build() {
            if (codigoInterno == null || codigoInterno.isBlank())
                throw new IllegalStateException("El código interno del mecánico es obligatorio.");
            if (nombreCompleto == null || nombreCompleto.isBlank())
                throw new IllegalStateException("El nombre del mecánico es obligatorio.");
            if (especialidad == null || especialidad.isBlank())
                throw new IllegalStateException("La especialidad del mecánico es obligatoria.");
            if (numeroCertificacion == null || numeroCertificacion.isBlank())
                throw new IllegalStateException("El número de certificación es obligatorio.");
            return new Mecanico(this);
        }
    }
}