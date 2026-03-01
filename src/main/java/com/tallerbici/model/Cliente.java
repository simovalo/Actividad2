package com.tallerbici.model;

import com.tallerbici.model.interfaces.IRegistrable;
import com.tallerbici.model.interfaces.IConsultable;

/**
 * Representa un cliente del taller de bicicletas.
 * Principio S (SRP): solo maneja datos del cliente.
 * Principio O (OCP): extensible sin modificar esta clase.
 * Usa el patrón Builder para su construcción.
 *
 * @author Equipo TallerBici - Programación II
 */
public class Cliente implements IRegistrable, IConsultable {

    // Los atributos son final para garantizar inmutabilidad (buena práctica)
    private final String numeroCedula;
    private final String nombreCompleto;
    private final String telefono;
    private final String direccion;


    /**
     * Constructor privado: solo se puede crear un Cliente usando el Builder.
     */
    private Cliente(Builder builder) {
        this.numeroCedula  = builder.numeroCedula;
        this.nombreCompleto = builder.nombreCompleto;
        this.telefono       = builder.telefono;
        this.direccion      = builder.direccion;
    }

    // ======================== GETTERS ========================

    public String getNumeroCedula()  { return numeroCedula; }
    public String getNombreCompleto(){ return nombreCompleto; }
    public String getTelefono()      { return telefono; }
    public String getDireccion()     { return direccion; }

    // =================== IRegistrable =======================

    @Override
    public String getId() {
        return numeroCedula;
    }

    @Override
    public String getResumen() {
        return nombreCompleto + " (CC: " + numeroCedula + ")";
    }

    // =================== IConsultable =======================

    @Override
    public String getDetalles() {
        return "Cliente: " + nombreCompleto +
                "\nCédula: " + numeroCedula +
                "\nTeléfono: " + telefono +
                "\nDirección: " + direccion;
    }

    @Override
    public boolean coincideCon(String criterio) {
        String c = criterio.toLowerCase();
        return nombreCompleto.toLowerCase().contains(c)
                || numeroCedula.toLowerCase().contains(c)
                || telefono.toLowerCase().contains(c);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // ====================== BUILDER =========================

    /**
     * Patrón Builder para construir un Cliente de forma legible y segura.
     * Principio S: el Builder tiene la única responsabilidad de construir el Cliente.
     */
    public static class Builder {

        private String numeroCedula;
        private String nombreCompleto;
        private String telefono;
        private String direccion;

        public Builder numeroCedula(String numeroCedula) {
            this.numeroCedula = numeroCedula;
            return this;
        }

        public Builder nombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        /**
         * Construye el Cliente validando los campos obligatorios.
         * @return nuevo objeto Cliente
         * @throws IllegalStateException si falta algún campo obligatorio
         */
        public Cliente build() {
            if (numeroCedula == null || numeroCedula.isBlank())
                throw new IllegalStateException("La cédula del cliente es obligatoria.");
            if (nombreCompleto == null || nombreCompleto.isBlank())
                throw new IllegalStateException("El nombre del cliente es obligatorio.");
            if (telefono == null || telefono.isBlank())
                throw new IllegalStateException("El teléfono del cliente es obligatorio.");
            if (direccion == null || direccion.isBlank())
                throw new IllegalStateException("La dirección del cliente es obligatoria.");
            return new Cliente(this);
        }
    }
}