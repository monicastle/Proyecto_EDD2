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
public class BTree implements Serializable {

    int orden;
    int raiz;
    ArrayList<Node> nodos;
    private static final long serialVersionUID = 6529685098267757690L;

    public BTree(int orden) {
        nodos = new ArrayList<Node>();
        this.orden = orden;
        nodos.add(new Node(orden));
        raiz = 0;
        //this.t = (int) Math.ceil((orden + 1) / 2);// responsabilizar a Jose.

    }

    /*public BTree(int orden, String llave, long pos) {
        //this.t = (int) Math.floor((orden + 1) / 2);// responsabilizar a Jose.
        this.orden = orden;
        raiz = new Node(orden, llave, pos);
    }*/
    public int getM() {
        return orden;
    }

    public void setM(int orden) {
        this.orden = orden;
    }

    public int getRaiz() {
        return raiz;
    }

    public void setRaiz(int raiz) {
        this.raiz = raiz;
    }

    public int upperBKeys_order() {
        return orden - 1;
    }

    public int lowerBKeys_order() {
        return Math.max((orden / 2) - 1, 1);
    }

    public int UpperBChild() {
        return orden;
    }

    public NodoIndice B_Tree_Search(int ix, String k) {
        int i = 0;
        Node x = nodos.get((int) ix);
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
            i++;
        }
        if (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) == 0) {
            return new NodoIndice(x, i);
        }
        if (x.isLeaf()) {
            return null;
        } else {
            return B_Tree_Search(x.getHijos().get(i), k);
        }
    }//fin método

    public void searchByAffinity(int ix, String k, ArrayList<Long> rrns) {
        int i = 0;
        Node x = nodos.get((int) ix);
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
            i++;
        }
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) == 0) {
            rrns.add(x.getLlaves().get(i).getPos());
            if (!x.isLeaf()) {
                searchByAffinity(x.getHijos().get(i), k, rrns);
            }
            i++;
        }
        if (!x.isLeaf()) {
            searchByAffinity(x.getHijos().get(i), k, rrns);
        }
    }

    public void insert(String k, long p) {
        int ir = raiz;
        Node r = nodos.get(this.getRaiz());
        //System.out.println(r.getLlaves().size());
        //System.out.println(raiz.getLlaves().size());
        if (r.getN() == upperBKeys_order()) {
            int is = nodos.size();
            Node s = new Node(orden);
            nodos.add(s);
            raiz = is;
            s.setLeaf(false);
            s.setN(0);
            s.getHijos().set(0, ir);
            B_Tree_Split_Child(is, 0, ir);
            B_Tree_Insert_NonFull(is, k, p);
        } else {
            B_Tree_Insert_NonFull(ir, k, p);
        }
    }

    public void B_Tree_Split_Child(int ix, int i, int iy) {
        int iz = nodos.size();
        Node z = new Node(orden);
        nodos.add(z);
        Node y = nodos.get(iy);
        Node x = nodos.get(ix);
        z.setLeaf(y.isLeaf());
        z.setN(this.upperBKeys_order() - this.lowerBKeys_order() - 1);
        for (int j = 0; j < z.getN(); j++) {
            z.getLlaves().set(j, y.getLlaves().get(j + this.lowerBKeys_order() + 1));
        }
        if (!y.isLeaf()) {
            for (int j = 0; j < z.getN() + 1; j++) {
                z.getHijos().set(j, y.getHijos().get(j + this.lowerBKeys_order() + 1));
            }
        }
        y.setN(this.lowerBKeys_order());
        x.getHijos().add(i + 1, iz);
        x.getHijos().remove(orden);

        x.getLlaves().add(i, y.getLlaves().get(this.lowerBKeys_order()));
        x.getLlaves().remove(this.upperBKeys_order());

        x.setN(x.getN() + 1);

    }

    public void B_Tree_Insert_NonFull(int ix, String k, long p) {
        Node x = nodos.get(ix);
        int i = x.getN() - 1;
        if (x.isLeaf()) {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            }
            x.getLlaves().add(i + 1, new LlavePos(k, p));
            x.getLlaves().remove(this.upperBKeys_order());
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            }
            i++;

            if (nodos.get(x.getHijos().get(i)).getN() == this.upperBKeys_order()) {
                B_Tree_Split_Child(ix, i, x.getHijos().get(i));
                if (k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
                    i++;
                }
            }
            B_Tree_Insert_NonFull(x.getHijos().get(i), k, p);
        }

    }

    public void imprimir_arbol(int ina, int num) {
        Node nodo_actual = nodos.get(ina);
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

    public BTree cargarArbol(String nombre) {
        File archivo = new File(nombre + "keyTree");
        try {
            if (archivo.exists()) {
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try {
                    BTree arbolTemp = (BTree) objeto.readObject();
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

    public void B_Tree_Delete(int ix, String key) {
        Node x = nodos.get(ix);
        int pos = x.Find(key);
        if (pos != -1) {
            if (x.isLeaf()) {
                int i = 0;
                for (i = 0; i < x.getN() && x.getLlaves().get(i).getLlave().compareTo(key) != 0; i++) {
                }
                for (; i < x.getN(); i++) {
                    if (i != orden - 2) {
                        x.getLlaves().set(i, x.getLlaves().get(i + 1));
                    }
                }
                x.setN(x.getN() - 1);
                return;
            }
            if (!x.isLeaf()) {

                int ipred = x.getHijos().get(pos);
                Node prednode = nodos.get(ipred);
                String pred_llave = "";
                long pos_pred = -1;
                if (prednode.getN() >= lowerBKeys_order() + 1) {
                    for (;;) {
                        if (prednode.isLeaf()) {
                            pred_llave = prednode.getLlaves().get(prednode.getN() - 1).getLlave();
                            pos_pred = prednode.getLlaves().get(prednode.getN() - 1).getPos();
                            break;
                        } else {
                            ipred = prednode.getHijos().get(prednode.getN());
                            prednode = nodos.get(ipred);
                        }
                    }
                    B_Tree_Delete(ix, key);
                    x.getLlaves().set(pos, new LlavePos(pred_llave, pos_pred));
                    return;
                }//fin if

                int inext = x.getHijos().get(pos + 1);
                Node nextNode = nodos.get(inext);
                if (nextNode.n >= lowerBKeys_order() + 1) {
                    String next_llave = nextNode.getLlaves().get(0).getLlave();
                    long next_pos = -1;
                    if (!nextNode.isLeaf()) {
                        inext = nextNode.getHijos().get(0);
                        nextNode = nodos.get(inext);
                        for (;;) {
                            if (nextNode.isLeaf()) {
                                next_llave = nextNode.getLlaves().get(nextNode.getN() - 1).getLlave();
                                next_pos = nextNode.getLlaves().get(nextNode.getN() - 1).getPos();
                                break;
                            } else {
                                inext = nextNode.getHijos().get(nextNode.getN());
                                nextNode = nodos.get(inext);
                            }
                        }
                    }
                    B_Tree_Delete(inext, next_llave);
                    x.getLlaves().set(pos, new LlavePos(next_llave, next_pos));
                    return;
                }

                int temp = prednode.getN() + 1;
                prednode.getLlaves().set(prednode.n++, x.getLlaves().get(pos));
                for (int i = 0, j = prednode.getN(); i < nextNode.getN(); i++) {
                    prednode.getLlaves().set(j++, nextNode.getLlaves().get(i));
                    prednode.n++;
                }
                for (int i = 0; i < nextNode.n + 1; i++) {
                    prednode.getHijos().set(temp++, nextNode.getHijos().get(i));
                }

                x.getHijos().set(pos, ipred);
                for (int i = pos; i < x.n; i++) {
                    if (i != orden - 2) {
                        x.getLlaves().set(i, x.getLlaves().get(i + 1));
                    }
                }
                for (int i = pos + 1; i < x.n + 1; i++) {
                    if (i != orden - 1) {
                        x.getHijos().set(i, x.getHijos().get(i + 1));
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
                B_Tree_Delete(ipred, key);
                return;
            }
        } else {
            for (pos = 0; pos < x.n; pos++) {
                if (x.getLlaves().get(pos).getLlave().compareTo(key) > 0) {
                    break;
                }
            }//fin for
            int i_temp = x.getHijos().get(pos);
            Node temporal = nodos.get(i_temp);
            if (temporal.n >= lowerBKeys_order() + 1) {
                B_Tree_Delete(i_temp, key);
                return;
            }//fn if
            if (true) {
                int inb = -1;
                Node nb = null;
                String divide = "";
                long dividePos = -1;

                if (pos != x.n && nodos.get(x.getHijos().get(pos + 1)).n >= lowerBKeys_order() + 1) {
                    divide = x.getLlaves().get(pos).getLlave();
                    dividePos = x.getLlaves().get(pos).getPos();
                    inb = x.getHijos().get(pos + 1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos, nb.getLlaves().get(0));
                    temporal.getLlaves().set(temporal.n++, new LlavePos(divide, dividePos));
                    temporal.getHijos().set(temporal.n, nb.getHijos().get(0));
                    for (int i = 1; i < nb.n; i++) {
                        nb.getLlaves().set(i - 1, nb.getLlaves().get(i));
                    }
                    for (int i = 1; i <= nb.n; i++) {
                        nb.getHijos().set(i - 1, nb.getHijos().get(i));
                    }
                    nb.n--;
                    B_Tree_Delete(i_temp, key);
                    return;
                } else if (pos != 0 && nodos.get(x.getHijos().get(pos - 1)).n >= lowerBKeys_order() + 1) {

                    divide = x.getLlaves().get(pos - 1).getLlave();
                    dividePos = x.getLlaves().get(pos - 1).getPos();
                    inb = x.getHijos().get(pos - 1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos - 1, nb.getLlaves().get(nb.n - 1));
                    int ichild = nb.getHijos().get(nb.n);
                    //Nodo child = nodos.get(ichild);
                    nb.n--;

                    for (int i = temporal.n; i > 0; i--) {
                        temporal.getLlaves().set(i, temporal.getLlaves().get(i - 1));
                    }
                    temporal.getLlaves().set(0, new LlavePos(divide, dividePos));
                    for (int i = temporal.n + 1; i > 0; i--) {
                        temporal.getHijos().set(i, temporal.getHijos().get(i - 1));
                    }
                    temporal.getHijos().set(0, ichild);
                    temporal.n++;
                    B_Tree_Delete(i_temp, key);
                    return;
                } else {
                    int ilt = -1;
                    Node lt = null;
                    int irt = -1;
                    Node rt = null;
                    boolean last = false;
                    if (pos != x.n) {
                        divide = x.getLlaves().get(pos).getLlave();
                        dividePos = x.getLlaves().get(pos).getPos();
                        ilt = x.getHijos().get(pos);
                        lt = nodos.get(ilt);
                        irt = x.getHijos().get(pos + 1);
                        rt = nodos.get(irt);
                    } else {
                        divide = x.getLlaves().get(pos - 1).getLlave();
                        dividePos = x.getLlaves().get(pos - 1).getPos();
                        irt = x.getHijos().get(pos);
                        rt = nodos.get(irt);
                        ilt = x.getHijos().get(pos - 1);
                        lt = nodos.get(ilt);
                        last = true;
                        pos--;
                    }//fin else
                    for (int i = pos; i < x.n - 1; i++) {
                        x.getLlaves().set(i, x.getLlaves().get(i + 1));
                    }//fin for
                    for (int i = pos + 1; i < x.n; i++) {
                        x.getHijos().set(i, x.getHijos().get(i + 1));
                    }//fin for
                    x.n--;
                    lt.getLlaves().set(lt.n++, new LlavePos(divide, dividePos));

                    for (int i = 0, j = lt.n; i < rt.n + 1; i++, j++) {
                        if (i < rt.n) {
                            lt.getLlaves().set(j, rt.getLlaves().get(i));
                        }
                        lt.getHijos().set(j, rt.getHijos().get(i));
                    }//fin for
                    lt.n += rt.n;
                    if (x.n == 0) {
                        if (ix == raiz) {
                            raiz = x.getHijos().get(0);
                        }
                        ix = x.getHijos().get(0);
                        x = nodos.get(ix);
                    }//fin if
                    B_Tree_Delete(ilt, key);
                    return;
                }
            }
        }
    }//termina el metodo eliminar

    public void BTree_KeysInOrder(int IndiceNodoActual, ArrayList<Long> lista) {
        if (IndiceNodoActual >= 0) {
            Node node = nodos.get(IndiceNodoActual);
            for (int i = 0; i < node.getN(); i++) {
                BTree_KeysInOrder(node.getHijos().get(i), lista);
                lista.add(node.getLlaves().get(i).getPos());
            }
            System.out.println("holaaa");
            BTree_KeysInOrder(node.getHijos().get(node.getN()), lista);//almenos con 3 registros este llamado es innesesario
        }//fin if
    }//fin método

}
