package taller.model;

import java.util.ArrayList;
import java.util.List;

/**
 * clase de una bicicleta registrada en el sistema.
 *
 * <p>identificada de forma única por su número de serie/marco y
 * obligatoriamente asociada a un Cliente propietario.</p>
 *
 * @author Samuel Marin
 * @version 1.0
 */
public class Bicicleta {

    /**
     * atributos.
     */
    public enum Tipo { RUTA, MTB, URBANA, ELECTRICA, OTRA }

    private String serial;
    private String marca;
    private Tipo tipo;
    private String color;
    private int anio;
    private final Cliente propietario;


    // Constructor

    /**
     * Crea una bicicleta con todos sus datos.
     *
     * @param serial      serial único (no nulo, no vacío).
     * @param marca       marca (no nula, no vacía).
     * @param tipo        tipo de bicicleta (no nulo).
     * @param color       color (no nulo, no vacío).
     * @param anio        año de fabricación (1900 – año actual).
     * @param propietario cliente dueño (no nulo).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Bicicleta(String serial, String marca, Tipo tipo,
                     String color, int anio, Cliente propietario) {
        Validaciones.requerido(serial,      "el serial de la bicicleta");
        Validaciones.requerido(marca,       "la marca de la bicicleta");
        Validaciones.noNulo(tipo,           "el tipo de bicicleta");
        Validaciones.requerido(color,       "el color de la bicicleta");
        Validaciones.anioFabricacion(anio);
        Validaciones.noNulo(propietario,    "el propietario de la bicicleta");

        this.serial      = serial.trim();
        this.marca       = marca.trim();
        this.tipo        = tipo;
        this.color       = color.trim();
        this.anio        = anio;
        this.propietario = propietario;
    }

    //  Getters

    public String  getSerial()      { return serial; }
    public String  getMarca()       { return marca; }
    public Tipo    getTipo()        { return tipo; }
    public String  getColor()       { return color; }
    public int     getAnio()        { return anio; }
    public Cliente getPropietario() { return propietario; }

    //  Setters

    public void setMarca(String marca) {
        Validaciones.requerido(marca, "La marca"); this.marca = marca.trim();
    }
    public void setColor(String color) {
        Validaciones.requerido(color, "El color"); this.color = color.trim();
    }
    public void setAnio(int anio) {
        Validaciones.anioFabricacion(anio); this.anio = anio;
    }

    /**
     * @return especificaciones
     */
    @Override
    public String toString() {
        return marca + " " + tipo + " — " + serial;
    }
}
