/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.util.ArrayList;

/**
 *
 * @author Monica
 */
public class Archivo {
    private ArrayList<Campo> campos = new ArrayList();

    public Archivo() {
    } // Fin Constructor Archivo

    public ArrayList<Campo> getCampos() {
        return campos;
    } // Fin Get Campos

    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    } // Fin Set Campos

    @Override
    public String toString() {
        return "Archivo{" + "campos=" + campos + '}';
    } // Fin ToString
    
} // Fin Clase Archivo
