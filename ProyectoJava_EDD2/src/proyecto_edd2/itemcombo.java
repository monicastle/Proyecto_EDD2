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
public class itemcombo {

    private String campo;
    private int pos;

    public itemcombo(String c, int p) {
        this.campo = c;
        this.pos = p;
    }

    @Override
    public String toString() {
        return campo;
    }

    public String getCampo() {
        return campo;
    }

    public int getPos() {
        return pos;
    }

}
