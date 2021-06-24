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
public class Archivo implements Serializable {

    private File archivo;
    private int ID;
    private int cant_regisros;
    private ArrayList<Campo> campos = new ArrayList();
    private static final long SerialVersionUID = 777L;
    private LinkedList AvailList = new LinkedList();
    //private String nombre;

    public Archivo() {
    } // Fin Constructor Archivo

    public Archivo(File archivo, int ID) {
        this.archivo = archivo;
        this.ID = ID;
        cant_regisros = 0;
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

    public void addCampo(Campo ca) {
        this.campos.add(ca);
    } // Fin Set Campo

    public void RemoveCampo(Campo ca) {
        this.campos.remove(ca);
    } // Fin Remove Campo

    public void setAvailList(LinkedList AvailList) {
        this.AvailList = AvailList;
    }

    public LinkedList getAvailList() {
        return AvailList;
    }

    public int getCant_regisros() {
        return cant_regisros;
    }

    public void setCant_regisros(boolean incrementa) {
        if (incrementa) {
            this.cant_regisros++;
        } else {
            this.cant_regisros--;
        }
    }

    public double getLongitudLLavePrimaria() {
        int retornar = 0;
        for (int i = 0; i < campos.size(); i++) {
            if (campos.get(i).isLlavePrimaria()) {
                retornar = campos.get(i).getLongitud();
                break;
            }
        }
        return retornar;
    }

    public double getLongitudLlaveSecundaria() {
        int retornar = 0;
        for (int i = 0; i < campos.size(); i++) {
            if (campos.get(i).isLlave_secundaria()) {
                retornar = campos.get(i).getLongitud();
                break;
            } // Fin If
        } // Fin For
        return retornar;
    } // Fin Get Longitud Llave Secundaria

    public int getSizeMetadata() {
        String metadata = "";
        for (Campo campo : this.getCampos()) {
            metadata += campo.getNombre() + "¡" + campo.getTipo_de_dato() + "¡" + campo.getLongitud() + "¡";
            if (campo.isLlavePrimaria()) {
                metadata += "Si¡";
            } else {
                metadata += "No¡";
            } // Fin If
            if (campo.isLlave_secundaria()) {
                metadata += "Si&";
            } else {
                metadata += "No&";
            } // Fin If
        } // Fin For
        return metadata.length() + 1;
    } // Gin Get Size Metadata

    public int longitud_fija_campos() {
        int length = 0;
        for (Campo campo : campos) {
            length += campo.getLongitud();
        }
        return length + campos.size();
    }

} // Fin Clase Archivo
