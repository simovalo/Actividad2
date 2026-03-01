package com.tallerbici.model;

import com.tallerbici.model.enums.TipoBicicleta;
import com.tallerbici.model.interfaces.IRegistrable;
import com.tallerbici.model.interfaces.IConsultable;

/**
 * Representa una bicicleta asociada a un cliente del taller.
 * Principio S (SRP): solo maneja datos de la bicicleta.
 * Usa el patrón Builder para su construcción.
 *
 * @author Equipo TallerBici - Programación II
 */
public class Bicicleta implements IRegistrable, IConsultable {

    private final String numeroSerial;
    private final String marca;
    private final TipoBicicleta tipo;
    private final String color;
    private final int anio;
    private final String cedulaCliente; // relación con el dueño
    private Cliente ownedByCliente;

    private Bicicleta(Builder builder) {
        this.numeroSerial   = builder.numeroSerial;
        this.marca          = builder.marca;
        this.tipo           = builder.tipo;
        this.color          = builder.color;
        this.anio           = builder.anio;
        this.cedulaCliente  = builder.cedulaCliente;
    }

    // ======================== GETTERS ========================

    public String getNumeroSerial()  { return numeroSerial; }
    public String getMarca()         { return marca; }
    public TipoBicicleta getTipo()   { return tipo; }
    public String getColor()         { return color; }
    public int getAnio()             { return anio; }
    public String getCedulaCliente() { return cedulaCliente; }

    // =================== IRegistrable =======================

    @Override
    public String getId() {
        return numeroSerial;
    }

    @Override
    public String getResumen() {
        return marca + " " + tipo.getDescripcion() + " (Serial: " + numeroSerial + ")";
    }

    // =================== IConsultable =======================

    @Override
    public String getDetalles() {
        return "Bicicleta: " + marca +
                "\nTipo: " + tipo.getDescripcion() +
                "\nColor: " + color +
                "\nSerial: " + numeroSerial +
                "\nAño: " + anio +
                "\nDueño (cédula): " + cedulaCliente;
    }

    @Override
    public boolean coincideCon(String criterio) {
        String c = criterio.toLowerCase();
        return numeroSerial.toLowerCase().contains(c)
                || marca.toLowerCase().contains(c)
                || color.toLowerCase().contains(c);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // ====================== BUILDER =========================

    /**
     * Builder para construir una Bicicleta de forma segura.
     */
    public static class Builder {

        private String numeroSerial;
        private String marca;
        private TipoBicicleta tipo;
        private String color;
        private int anio;
        private String cedulaCliente;

        public Builder numeroSerial(String numeroSerial) {
            this.numeroSerial = numeroSerial;
            return this;
        }

        public Builder marca(String marca) {
            this.marca = marca;
            return this;
        }

        public Builder tipo(TipoBicicleta tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder anio(int anio) {
            this.anio = anio;
            return this;
        }

        public Builder cedulaCliente(String cedulaCliente) {
            this.cedulaCliente = cedulaCliente;
            return this;
        }

        public Bicicleta build() {
            if (numeroSerial == null || numeroSerial.isBlank())
                throw new IllegalStateException("El número serial de la bicicleta es obligatorio.");
            if (marca == null || marca.isBlank())
                throw new IllegalStateException("La marca de la bicicleta es obligatoria.");
            if (tipo == null)
                throw new IllegalStateException("El tipo de bicicleta es obligatorio.");
            if (cedulaCliente == null || cedulaCliente.isBlank())
                throw new IllegalStateException("La cédula del cliente dueño es obligatoria.");
            if (anio < 1900 || anio > 2100)
                throw new IllegalStateException("El año de la bicicleta no es válido.");
            return new Bicicleta(this);
        }
    }
}