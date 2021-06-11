/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Monica
 */
public class Archivoconarbolb implements Serializable {

    private File archivo;
    private int ID;
    private ArbolB arbol;
  private String nombre;
    public Archivoconarbolb() {
    } // Fin Constructor Archivo

    public Archivoconarbolb(File archivo, int ID) {
        this.archivo = archivo;
        this.ID = ID;
    } // Fin Constructor Archivo

    public ArbolB getArbol() {
        return arbol;
    }

    public void setArbol(int orden) {
        this.arbol = new ArbolB(orden);
    }

    public int getID() {
        return ID;
    } // Fin Get ID;

    public void setID(int ID) {
        this.ID = ID;
    } // Fin Set ID;

    public File getArchivo() {
        return archivo;
    } // Fin Get Archivo

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    } // Fin Set Archivo


} // Fin Clase Archivo
