/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author diego
 */
public class Archivoarbolsecundario implements Serializable {

    private static final long SerialVersionUID = 777777L;

    private File archivo;
    private int IDArchivoActual;
    private BTree arbolSecundario;

    public Archivoarbolsecundario() {

    } // Fin Constructor Archivo Arbol Secundario

    public Archivoarbolsecundario(File archivo, int IDArchivoActual, BTree arbolSecundario) {
        this.archivo = archivo;
        this.IDArchivoActual = IDArchivoActual;
        this.arbolSecundario = arbolSecundario;
    } // Fin Constructor Archivo Arbol Secundario

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public int getIDArchivoActual() {
        return IDArchivoActual;
    } // Fin Get ID Archivo Actual

    public void setIDArchivoActual(int IDArchivoActual) {
        this.IDArchivoActual = IDArchivoActual;
    } // Fin Set ID Archivo Actual

    public BTree getArbolSecundario() {
        return arbolSecundario;
    } // Fin Get Arbol Secundario

    public void setArbolSecundario(BTree arbolSecundario) {
        this.arbolSecundario = arbolSecundario;
    } // Fin Set Arbol Secundario
}
