package taller.model;

/**
 * Representa una bicicleta registrada en el sistema del taller.
 *
 * <p>Identificada de forma única por su número de serie/marco y
 * obligatoriamente asociada a un {@link Cliente} propietario.</p>
 *
 * @author Samuel Marin
 * @version 1.0
 */
public class Bicicleta {

    /**
     * Categoría o tipo de bicicleta.
     */
    public enum Tipo { RUTA, MTB, URBANA, ELECTRICA, OTRA }

    /** Número de serie o marco único. */
    private String serial;

    /** Marca comercial de la bicicleta. */
    private String marca;

    /** Tipo o categoría. */
    private Tipo tipo;

    /** Color principal. */
    private String color;

    /** Año de fabricación (entre 1900 y el año actual). */
    private int anio;

    /** Cliente propietario. No puede ser nulo. */
    private final Cliente propietario;

    // ── Constructor ────────────────────────────────────────────────────────────

    /**
     * Crea una bicicleta con todos sus datos.
     *
     * @param serial      Serial/marco único (no nulo, no vacío).
     * @param marca       Marca (no nula, no vacía).
     * @param tipo        Tipo de bicicleta (no nulo).
     * @param color       Color (no nulo, no vacío).
     * @param anio        Año de fabricación (1900 – año actual).
     * @param propietario Cliente dueño (no nulo).
     * @throws IllegalArgumentException si algún parámetro no pasa la validación.
     */
    public Bicicleta(String serial, String marca, Tipo tipo,
                     String color, int anio, Cliente propietario) {
        Validaciones.requerido(serial,      "El serial de la bicicleta");
        Validaciones.requerido(marca,       "La marca de la bicicleta");
        Validaciones.noNulo(tipo,           "El tipo de bicicleta");
        Validaciones.requerido(color,       "El color de la bicicleta");
        Validaciones.anioFabricacion(anio);
        Validaciones.noNulo(propietario,    "El propietario de la bicicleta");

        this.serial      = serial.trim();
        this.marca       = marca.trim();
        this.tipo        = tipo;
        this.color       = color.trim();
        this.anio        = anio;
        this.propietario = propietario;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    /** @return número de serie o marco. */
    public String  getSerial()      { return serial; }

    /** @return marca comercial. */
    public String  getMarca()       { return marca; }

    /** @return tipo de bicicleta. */
    public Tipo    getTipo()        { return tipo; }

    /** @return color de la bicicleta. */
    public String  getColor()       { return color; }

    /** @return año de fabricación. */
    public int     getAnio()        { return anio; }

    /** @return cliente propietario. */
    public Cliente getPropietario() { return propietario; }

    // ── Setters ────────────────────────────────────────────────────────────────

    /** @param marca Nueva marca (no nula, no vacía). */
    public void setMarca(String marca) {
        Validaciones.requerido(marca, "La marca"); this.marca = marca.trim();
    }

    /** @param color Nuevo color (no nulo, no vacío). */
    public void setColor(String color) {
        Validaciones.requerido(color, "El color"); this.color = color.trim();
    }

    /** @param anio Nuevo año (1900 – año actual). */
    public void setAnio(int anio) {
        Validaciones.anioFabricacion(anio); this.anio = anio;
    }

    /**
     * @return cadena con formato "Marca TIPO — Serial".
     */
    @Override
    public String toString() {
        return marca + " " + tipo + " — " + serial;
    }
}
