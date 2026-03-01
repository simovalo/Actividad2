package com.tallerbici.model.enums;

/**
 * estados en que pueden estar las ordenes
 *
 * @author Samuel Marin - programación II
 */
public enum EstadoOrden {
    INGRESADA("ingresada"),
    EN_PROCESO("esta en proceso"),
    LISTA("lista"),
    ENTREGADA("entragada"),
    CANCELADA("cancelada");

    private final String descripcion;

    EstadoOrden(String descripcion) {
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