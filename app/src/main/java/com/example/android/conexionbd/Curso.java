package com.example.android.conexionbd;

/**
 * Created by jlixerkun on 12/03/17.
 */

public class Curso {

    /* id del curso */
//    public final int id;

    /* Nombre del curso*/
    public final String nombre;

    public Curso(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
