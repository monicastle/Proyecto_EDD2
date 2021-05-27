/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Monica
 */
public class Archivo implements Serializable {

    private File archivo;
    private int ID;
    private ArrayList<Campo> campos = new ArrayList();
    private static final long SerialVersionUID = 777L;

    public Archivo() {
    } // Fin Constructor Archivo

    public Archivo(File archivo, int ID) {
        this.archivo = archivo;
        this.ID = ID;
    } // Fin Constructor Archivo

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

    public ArrayList<Campo> getCampos() {
        return campos;
    } // Fin Get Campos

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    } // Fin Set Campos

    public void SetCampo(Campo ca) {
        this.campos.add(ca);
    } // Fin Set Campo

    public void RemoveSeccionArch(Campo ca) {
        this.campos.remove(ca);
    } // Fin Remove Campo

} // Fin Clase Archivo
