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
//esta clase se crea con el proposito de poder guardar los archivo con registros creados
    //esta clase lo que hace es crear el archivo donde los registros de un determinado archivo se crean 
    private File archivo;
    private int numeroderegistros;//esta variable adquiere el numero de registros que tiene el archivo
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
        numeroderegistros++;//Aqui en este metodo solo se le suma uno ya que es la cantidad de registros que ingresa
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
