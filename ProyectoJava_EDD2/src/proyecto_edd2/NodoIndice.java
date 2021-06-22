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

    private Node nodo;
    private int indice;

    public NodoIndice(Node nodo, int indice) {
        this.nodo = nodo;
        this.indice = indice;
    }

    public Node getNodo() {
        return nodo;
    }

    public void setNodo(Node nodo) {
        this.nodo = nodo;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

}
