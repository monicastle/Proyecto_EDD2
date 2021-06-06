/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author daba5
 */
public class ArbolB implements  Serializable {
    
    int m;// orden del arbol
    int raiz;
    ArrayList<Nodo> nodos;
    private static final long serialVersionUID = 6529685098267757690L;

    public ArbolB(int orden) {
        nodos = new ArrayList<Nodo>();
        this.m = orden;
        nodos.add(new Nodo(m));
        raiz = 0;
        //this.t = (int) Math.ceil((orden + 1) / 2);// responsabilizar a Jose.
        
    }

    /*public ArbolB(int orden, String llave, long pos) {
        //this.t = (int) Math.floor((orden + 1) / 2);// responsabilizar a Jose.
        this.m = orden;
        raiz = new Nodo(m, llave, pos);

    }*/

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getRaiz() {
        return raiz;
    }

    public void setRaiz(int raiz) {
        this.raiz = raiz;
    }

    public int upperBKeys() {
        return m - 1;
    }

    public int lowerBKeys() {

        return Math.max((m/2)-1, 1);

    }

    public int UpperBChild() {
        return m;
    }

    public NodoIndice B_Tree_Search(int ix, String k) {

        int i = 0;
        Nodo x = nodos.get((int) ix);

        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
            i++;
        }

        if (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) == 0) {
            return new NodoIndice(x, i);
        }
        if (x.isLeaf()) {
            return null;
        } else {
            return B_Tree_Search(x.getHijos().get(i),k);
        }
    }

    public void insert(String k, long p) {
        int ir = raiz;
        Nodo r = nodos.get(this.getRaiz());
        //System.out.println(r.getLlaves().size());
        //System.out.println(raiz.getLlaves().size());
        if (r.getN() == upperBKeys()) {
            int is = nodos.size();
            Nodo s = new Nodo(m);
            nodos.add(s);
            raiz = is;
            s.setLeaf(false);
            s.setN(0);
            s.getHijos().set(0, ir);
            B_Tree_Split_Child(is, 0, ir);
            B_Tree_Insert_NonFull(is,k,p);
        } else {
            B_Tree_Insert_NonFull(ir,k,p);
        }
    }

    public void B_Tree_Split_Child(int ix, int i, int iy) {
        int iz = nodos.size();
        Nodo z = new Nodo(m);
        nodos.add(z);
        Nodo y = nodos.get(iy);
        Nodo x = nodos.get(ix);
        z.setLeaf(y.isLeaf());
        z.setN(this.upperBKeys() - this.lowerBKeys() - 1);
        for (int j = 0; j < z.getN(); j++) {
            z.getLlaves().set(j, y.getLlaves().get(j + this.lowerBKeys() + 1));
        }
        if (!y.isLeaf()) {
            for (int j = 0; j < z.getN() + 1; j++) {
                z.getHijos().set(j, y.getHijos().get(j + this.lowerBKeys() + 1));
            }
        }
        y.setN(this.lowerBKeys());
        x.getHijos().add(i + 1, iz);
        x.getHijos().remove(m);

        x.getLlaves().add(i, y.getLlaves().get(this.lowerBKeys()));
        x.getLlaves().remove(this.upperBKeys());

        x.setN(x.getN() + 1);

    }

    public void B_Tree_Insert_NonFull(int ix, String k, long p) {
        Nodo x = nodos.get(ix);
        int i = x.getN() - 1;
        if (x.isLeaf()) {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            }
            x.getLlaves().add(i + 1, new LlavePos(k, p));
            x.getLlaves().remove(this.upperBKeys());
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            }
            i++;

            if (nodos.get(x.getHijos().get(i)).getN() == this.upperBKeys()) {
                B_Tree_Split_Child(ix, i, x.getHijos().get(i));
                if (k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
                    i++;
                }
            }
            B_Tree_Insert_NonFull(x.getHijos().get(i),k,p);
        }

    }


    public void imprimir_arbol(int ina, int num) {
        Nodo nodo_actual = nodos.get(ina);
        //se debe iniciar num en 0 a la hora de llamar el metodo
        String indent = new String(new char[1024]).replace('\0', ' ');
        for (int i = 0; i < nodo_actual.getN(); i++) {
            if (nodo_actual.isLeaf() == false && nodo_actual.getHijos().get(i) != null) {
                imprimir_arbol(nodo_actual.getHijos().get(i), num + 1);
            }
            if (nodo_actual.getLlaves().get(i) != null) {
                System.out.println(indent.substring(0, num * 4) + "llave: " + nodo_actual.getLlaves().get(i) + ", nivel:" + num);
            }

        }
        //si no es hoja, se llama el metodo recursivo pero ahora con su hijo
        if (nodo_actual.isLeaf() == false) {
            imprimir_arbol(nodo_actual.getHijos().get(nodo_actual.getN()), num + 1);
        }
    }
    
    public void traverseKeysInOrder (int inode, ArrayList<Long> lista) {
        if (inode >= 0) {
            Nodo node = nodos.get(inode);

            for (int i = 0; i < node.getN(); i++) {
                traverseKeysInOrder(node.getHijos().get(i), lista);
                lista.add(node.getLlaves().get(i).getPos());
            }
            traverseKeysInOrder(node.getHijos().get(node.getN()), lista);
        }
    }
    
   
    
    public ArbolB cargarArbol(String nombre) {
        File archivo = new File(nombre + "keyTree");
        try {
            if (archivo.exists()) {
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try {
                    ArbolB arbolTemp = (ArbolB) objeto.readObject();
                    return arbolTemp;
                } catch (EOFException e) {
                    //encontro el final del archivo
                }
                objeto.close();
                entrada.close();
            }            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
      
}
