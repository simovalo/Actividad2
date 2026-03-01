package com.tallerbici.model.interfaces;

/**
 * interfaz para entidades que pueden ser consultadas.
 * principio I (ISP)
 *
 * @author Equipo TallerBici - Programación II
 */
public interface IConsultable {

    /**
     * muestra los datos completos de la entidad.
     * @return String con toda la información
     */
    String getDetalles();

    /**
     * Verifica si la entidad coincide con un criterio de búsqueda.
     * @param criterio texto que busca
     * @return true si esta contiene, false si no
     */
    boolean coincideCon(String criterio);
}