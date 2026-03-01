package com.tallerbici.model.interfaces;

/**
 * Interfaz para entidades que pueden ser registradas en el sistema.
 * Principio I (ISP): interfaz pequeña y específica.
 * Principio D (DIP): las clases de alto nivel dependen de esta abstracción.
 *
 * @author Equipo TallerBici - Programación II
 */
public interface IRegistrable {

    /**
     * Retorna el identificador único de la entidad.
     * @return String con el identificador
     */
    String getId();

    /**
     * Retorna una representación resumida para mostrar en listas.
     * @return String con el resumen
     */
    String getResumen();
}