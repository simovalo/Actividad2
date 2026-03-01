package com.tallerbici.model.enums;

/**
 * define los tipos de bicicletas
 *
 * @author Simon Valencia - Programación II
 */
public enum TipoBicicleta {
    RUTA("Ruta"),
    MTB("MTB / Mountain Bike"),
    URBANA("Urbana"),
    ELECTRICA("Eléctrica"),
    BMX("BMX"),
    HIBRIDA("Híbrida");

    private final String descripcion;

    TipoBicicleta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}