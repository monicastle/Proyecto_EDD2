/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

/**
 *
 * @author Monica
 */
public class Campo {
    private String nombre;
    private int tipo_de_dato;
    private int longitud;
    private boolean llaveprimaria;

    public Campo() {
    } // Fin Constructor Campo

    public Campo(String nombre, int tipo_de_dato, int longitud, boolean llaveprimaria) {
        this.nombre = nombre;
        this.tipo_de_dato = tipo_de_dato;
        this.longitud = longitud;
        this.llaveprimaria = llaveprimaria;
    } // Fin Constructor Campo

    public String getNombre() {
        return nombre;
    } // Fin Get Nombre

    public void setNombre(String nombre) {
        this.nombre = nombre;
    } // Fin Set Nombre

    public int getTipo_de_dato() {
        return tipo_de_dato;
    } // Fin Get Tipo de Dato

    public void setTipo_de_dato(int tipo_de_dato) {
        this.tipo_de_dato = tipo_de_dato;
    } // Fin Set Tipo de Dato

    public int getLongitud() {
        return longitud;
    } // Fin Get Longitud

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    } // Fin Set Longitud

    public boolean isLlaveprimaria() {
        return llaveprimaria;
    } // Fin Is Llave Primaria

    public void setLlaveprimaria(boolean llaveprimaria) {
        this.llaveprimaria = llaveprimaria;
    } // Fin Set Llave Primaria

    @Override
    public String toString() {
        return "Campo{" + "nombre=" + nombre + ", tipo_de_dato=" + tipo_de_dato + ", longitud=" + longitud + ", llaveprimaria=" + llaveprimaria + '}';
    } // Fin ToString
    
} // Fin Clase Campo
