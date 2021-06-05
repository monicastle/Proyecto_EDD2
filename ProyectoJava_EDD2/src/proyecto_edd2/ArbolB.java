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
    
    private void Remove(int ix, String key) {
        Nodo x = nodos.get(ix);
        int pos = x.Find(key);
        if (pos != -1) {
            if (x.isLeaf()) {
                int i = 0;
                for (i = 0; i < x.getN() && x.getLlaves().get(i).getLlave().compareTo(key) != 0; i++) {
                }
                ;
                for (; i < x.getN(); i++) {
                    if (i != m - 2) {
                        x.getLlaves().set(i, x.getLlaves().get(i+1));
                    }
                }
                x.setN(x.getN()-1);
                return;
            }
            if (!x.isLeaf()) {
                
                int ip = x.getHijos().get(pos);
                Nodo pred = nodos.get(ip);
                String predKey = "";
                long predPos = -1;
                if (pred.getN() >= lowerBKeys()+1) {
                    for (;;) {
                        if (pred.isLeaf()) {
                            //System.out.println(pred.getN());
                            predKey = pred.getLlaves().get(pred.getN()-1).getLlave();
                            predPos = pred.getLlaves().get(pred.getN()-1).getPos();
                            break;
                        } else {
                            ip = pred.getHijos().get(pred.getN());
                            pred = nodos.get(ip);
                        }
                    }
                    Remove(ip, predKey);
                    x.getLlaves().set(pos, new LlavePos(predKey,predPos));
                    return;
                }

                
                int inn = x.getHijos().get(pos+1);
                Nodo nextNode = nodos.get(inn);
                if (nextNode.n >= lowerBKeys()+1) {
                    String nextKey = nextNode.getLlaves().get(0).getLlave();
                    long nextPos = -1;
                    if (!nextNode.isLeaf()) {
                        inn = nextNode.getHijos().get(0);
                        nextNode = nodos.get(inn);
                        for (;;) {
                            if (nextNode.leaf) {
                                nextKey = nextNode.getLlaves().get(nextNode.getN()-1).getLlave();
                                nextPos = nextNode.getLlaves().get(nextNode.getN()-1).getPos();
                                break;
                            } else {
                                inn = nextNode.getHijos().get(nextNode.getN());
                                nextNode = nodos.get(inn);
                            }
                        }
                    }
                    Remove(inn, nextKey);
                    x.getLlaves().set(pos, new LlavePos(nextKey, nextPos));
                    return;
                }

                int temp = pred.getN() + 1;
                pred.getLlaves().set(pred.n++,x.getLlaves().get(pos));
                for (int i = 0, j = pred.getN(); i < nextNode.getN(); i++) {
                    pred.getLlaves().set(j++, nextNode.getLlaves().get(i));
                    pred.n++;
                }
                for (int i = 0; i < nextNode.n + 1; i++) {
                    pred.getHijos().set(temp++, nextNode.getHijos().get(i));
                }

                x.getHijos().set(pos, ip);
                for (int i = pos; i < x.n; i++) {
                    if (i != m - 2) {
                        x.getLlaves().set(i, x.getLlaves().get(i+1));
                    }
                }
                for (int i = pos + 1; i < x.n + 1; i++) {
                    if (i != m - 1) {
                        x.getHijos().set(i, x.getHijos().get(i+1));
                    }
                }
                x.n--;
                if (x.n == 0) {
                    if (ix == raiz) {
                        raiz = x.getHijos().get(0);
                    }
                    ix = x.getHijos().get(0);
                    x = nodos.get(ix);
                }
                Remove(ip, key);
                return;
            }
        } else {
            for (pos = 0; pos < x.n; pos++) {
                if (x.getLlaves().get(pos).getLlave().compareTo(key) > 0) {
                    break;
                }
            }
            int itmp = x.getHijos().get(pos);
            Nodo tmp = nodos.get(itmp);
            if (tmp.n >= lowerBKeys()+1) {
                Remove(itmp, key);
                return;
            }
            if (true) {
                int inb = -1;
                Nodo nb = null;
                String divider = "";
                long dividerPos = -1;

                if (pos != x.n && nodos.get(x.getHijos().get(pos+1)).n >= lowerBKeys()+1) {
                    divider = x.getLlaves().get(pos).getLlave();
                    dividerPos = x.getLlaves().get(pos).getPos();
                    inb = x.getHijos().get(pos+1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos, nb.getLlaves().get(0));
                    tmp.getLlaves().set(tmp.n++, new LlavePos(divider, dividerPos));
                    tmp.getHijos().set(tmp.n, nb.getHijos().get(0));
                    for (int i = 1; i < nb.n; i++) {
                        nb.getLlaves().set(i-1, nb.getLlaves().get(i));
                    }
                    for (int i = 1; i <= nb.n; i++) {
                        nb.getHijos().set(i-1, nb.getHijos().get(i));
                    }
                    nb.n--;
                    Remove(itmp, key);
                    return;
                } else if (pos != 0 && nodos.get(x.getHijos().get(pos-1)).n >= lowerBKeys()+1) {

                    divider = x.getLlaves().get(pos-1).getLlave();
                    dividerPos = x.getLlaves().get(pos-1).getPos();
                    inb = x.getHijos().get(pos-1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos-1, nb.getLlaves().get(nb.n-1));
                    int ichild = nb.getHijos().get(nb.n);
                    //Nodo child = nodos.get(ichild);
                    nb.n--;

                    for (int i = tmp.n; i > 0; i--) {
                        tmp.getLlaves().set(i, tmp.getLlaves().get(i-1));
                    }
                    tmp.getLlaves().set(0, new LlavePos(divider, dividerPos));
                    for (int i = tmp.n + 1; i > 0; i--) {
                        tmp.getHijos().set(i, tmp.getHijos().get(i-1));
                    }
                    tmp.getHijos().set(0, ichild);
                    tmp.n++;
                    Remove(itmp, key);
                    return;
                } else {
                    int ilt = -1;
                    Nodo lt = null;
                    int irt = -1;
                    Nodo rt = null;
                    boolean last = false;
                    if (pos != x.n) {
                        divider = x.getLlaves().get(pos).getLlave();
                        dividerPos = x.getLlaves().get(pos).getPos();
                        ilt = x.getHijos().get(pos);
                        lt = nodos.get(ilt);
                        irt = x.getHijos().get(pos+1);
                        rt = nodos.get(irt);
                    } else {
                        divider = x.getLlaves().get(pos-1).getLlave();
                        dividerPos = x.getLlaves().get(pos-1).getPos();
                        irt = x.getHijos().get(pos);
                        rt = nodos.get(irt);
                        ilt = x.getHijos().get(pos-1);
                        lt = nodos.get(ilt);
                        last = true;
                        pos--;
                    }
                    for (int i = pos; i < x.n - 1; i++) {
                        x.getLlaves().set(i, x.getLlaves().get(i+1));
                    }
                    for (int i = pos + 1; i < x.n; i++) {
                        x.getHijos().set(i, x.getHijos().get(i+1));
                    }
                    x.n--;
                    lt.getLlaves().set(lt.n++, new LlavePos(divider, dividerPos));

                    for (int i = 0, j = lt.n; i < rt.n + 1; i++, j++) {
                        if (i < rt.n) {
                            lt.getLlaves().set(j, rt.getLlaves().get(i));
                        }
                        lt.getHijos().set(j, rt.getHijos().get(i));
                    }
                    lt.n += rt.n;
                    if (x.n == 0) {
                        if (ix == raiz) {
                            raiz = x.getHijos().get(0);
                        }
                        ix = x.getHijos().get(0);
                        x = nodos.get(ix);
                    }
                    Remove(ilt, key);
                    return;
                }
            }
        }
    }

    public void Remove(String key) {
        Nodo x = this.B_Tree_Search(raiz, key).getNodo();
        if (x == null) {
            return;
        }
        Remove(raiz, key);
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
    
    public void searchByAffinity(int ix, String k, ArrayList<Long> rrns) {
        
        int i = 0;
        Nodo x = nodos.get((int) ix);

        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
            i++;
        }
        
        boolean flag = false;
        
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) == 0) {
            flag = true;
            rrns.add(x.getLlaves().get(i).getPos());
            if(!x.isLeaf()) {
               searchByAffinity(x.getHijos().get(i), k, rrns); 
            }
            i++;
        }
        if (!x.isLeaf()) {
            searchByAffinity(x.getHijos().get(i), k, rrns);
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
