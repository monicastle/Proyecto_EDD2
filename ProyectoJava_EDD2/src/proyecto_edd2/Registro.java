/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.Serializable;

/**
 *
 * @author diego
 */
public class Registro implements Serializable {

    private String llave;
    private String resto;
    private boolean activo;

    public Registro() {
        llave = "nn";
        resto = "";
        activo=true;
    }

    public Registro(String llave, String resto) {
        this.llave = llave;
        this.resto = resto;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getResto() {
        return resto;
    }

    public void setResto(String resto) {
        this.resto = resto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getTamaño() {
        return getLlave().length() * 2 + 2 + 4 + 1;
    }
}
