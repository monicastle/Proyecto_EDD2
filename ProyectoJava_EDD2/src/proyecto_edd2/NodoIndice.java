/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

/**
 *
 * @author daba5
 */
public class NodoIndice {
    
      private Nodo nodo;
    private int indice;

    public NodoIndice(Nodo nodo, int indice) {
        this.nodo = nodo;
        this.indice = indice;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
    
    
}
