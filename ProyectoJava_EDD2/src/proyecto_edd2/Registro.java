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
public class Registro implements Serializable {

    private File archivo;
    private int numeroderegistros;
    private int ID;

    public Registro(File archivo, int ID) {
        this.archivo = archivo;
        this.ID = ID;
        numeroderegistros = 0;
    } // Fin Constructor Archivo

    public int getNumeroderegistros() {
        return numeroderegistros;
    }

    public void setNumeroderegistros(int numeroderegistros) {
        this.numeroderegistros = numeroderegistros;
    }

    public void addregistro() {
        numeroderegistros++;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

}
