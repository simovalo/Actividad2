package com.tallerbici.model;

import com.tallerbici.model.interfaces.IRegistrable;
import com.tallerbici.model.interfaces.IConsultable;

/**
 * Representa un repuesto del taller (funcionalidad propia: gestión de stock).
 * Si el stock cae por debajo del mínimo, el sistema emite una alerta.
 * Principio S (SRP): solo maneja datos del repuesto y su stock.
 *
 * @author Equipo TallerBici - Programación II
 */
public class Repuesto implements IRegistrable, IConsultable {

    // Stock mínimo por defecto para activar alerta
    private static final int STOCK_MINIMO_DEFAULT = 3;

    private final String codigo;
    private final String nombre;
    private final String categoria;
    private final double precioUnitario;
    private final int stockMinimo;
    private int cantidadDisponible;

    private Repuesto(Builder builder) {
        this.codigo              = builder.codigo;
        this.nombre              = builder.nombre;
        this.categoria           = builder.categoria;
        this.precioUnitario      = builder.precioUnitario;
        this.stockMinimo         = builder.stockMinimo;
        this.cantidadDisponible  = builder.cantidadDisponible;
    }

    // ======================== GETTERS ========================

    public String getCodigo()           { return codigo; }
    public String getNombre()           { return nombre; }
    public String getCategoria()        { return categoria; }
    public double getPrecioUnitario()   { return precioUnitario; }
    public int getStockMinimo()         { return stockMinimo; }
    public int getCantidadDisponible()  { return cantidadDisponible; }

    // ======================== LÓGICA =========================

    /**
     * Indica si el stock está por debajo del mínimo (alerta).
     * @return true si hay alerta de stock bajo
     */
    public boolean tieneStockBajo() {
        return cantidadDisponible <= stockMinimo;
    }

    /**
     * Agrega unidades al stock.
     * @param cantidad unidades a agregar (debe ser positivo)
     */
    public void agregarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva.");
        this.cantidadDisponible += cantidad;
    }

    /**
     * Descuenta unidades del stock (cuando se usa en una orden).
     * @param cantidad unidades a usar
     */
    public void usarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva.");
        if (cantidad > cantidadDisponible)
            throw new IllegalStateException("No hay suficiente stock de: " + nombre);
        this.cantidadDisponible -= cantidad;
    }

    // =================== IRegistrable =======================

    @Override
    public String getId() { return codigo; }

    @Override
    public String getResumen() {
        return "[" + codigo + "] " + nombre + " | Stock: " + cantidadDisponible
                + (tieneStockBajo() ? " ⚠ STOCK BAJO" : "");
    }

    // =================== IConsultable =======================

    @Override
    public String getDetalles() {
        return "Repuesto: " + nombre +
                "\nCódigo: " + codigo +
                "\nCategoría: " + categoria +
                "\nPrecio: $" + String.format("%.2f", precioUnitario) +
                "\nStock disponible: " + cantidadDisponible +
                "\nStock mínimo: " + stockMinimo +
                (tieneStockBajo() ? "\n⚠ ALERTA: Stock por debajo del mínimo" : "");
    }

    @Override
    public boolean coincideCon(String criterio) {
        String c = criterio.toLowerCase();
        return nombre.toLowerCase().contains(c)
                || codigo.toLowerCase().contains(c)
                || categoria.toLowerCase().contains(c);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // ====================== BUILDER =========================

    public static class Builder {

        private String codigo;
        private String nombre;
        private String categoria;
        private double precioUnitario;
        private int stockMinimo     = STOCK_MINIMO_DEFAULT;
        private int cantidadDisponible = 0;

        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder precioUnitario(double precioUnitario) {
            this.precioUnitario = precioUnitario;
            return this;
        }

        public Builder stockMinimo(int stockMinimo) {
            this.stockMinimo = stockMinimo;
            return this;
        }

        public Builder cantidadDisponible(int cantidadDisponible) {
            this.cantidadDisponible = cantidadDisponible;
            return this;
        }

        public Repuesto build() {
            if (codigo == null || codigo.isBlank())
                throw new IllegalStateException("El código del repuesto es obligatorio.");
            if (nombre == null || nombre.isBlank())
                throw new IllegalStateException("El nombre del repuesto es obligatorio.");
            if (precioUnitario < 0)
                throw new IllegalStateException("El precio no puede ser negativo.");
            return new Repuesto(this);
        }
    }
}