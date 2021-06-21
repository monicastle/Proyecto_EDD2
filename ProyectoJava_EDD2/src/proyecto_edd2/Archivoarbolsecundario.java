/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

/**
 *
 * @author diego
 */
public class Archivoarbolsecundario {
      private int ID;
    private int IDArchivoActual;
    private BTree arbolSecundario;

    public Archivoarbolsecundario() {

    } // Fin Constructor Archivo Arbol Secundario

    public Archivoarbolsecundario(int ID, int IDArchivoActual, BTree arbolSecundario) {
        this.ID = ID;
        this.IDArchivoActual = IDArchivoActual;
        this.arbolSecundario = arbolSecundario;
    } // Fin Constructor Archivo Arbol Secundario

    public int getID() {
        return ID;
    } // Fin Get ID

    public void setID(int ID) {
        this.ID = ID;
    } // Fin Set ID

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
