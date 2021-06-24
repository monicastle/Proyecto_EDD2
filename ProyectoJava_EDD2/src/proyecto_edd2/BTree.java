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
 * @author diego
 */
public class BTree implements Serializable {

    int orden;
    int raiz;
    ArrayList<Node> nodos;
    private static final long SerialVersionUID = 777L;

    public BTree(int orden) {
        nodos = new ArrayList<Node>();
        this.orden = orden;
        nodos.add(new Node(orden));
        raiz = 0;
    } // Fin Constructor BTree

    public int getOrden() {
        return orden;
    } // Fin Get Orden

    public void setOrden(int orden) {
        this.orden = orden;
    } // Fin Set Orden

    public int getRaiz() {
        return raiz;
    } // Fin Get Raiz

    public void setRaiz(int raiz) {
        this.raiz = raiz;
    } // Fin Set Raiz

    public ArrayList<Node> getNodos() {
        return nodos;
    } // Fin Get Nodos

    public void setNodos(ArrayList<Node> nodos) {
        this.nodos = nodos;
    } // Fin Set Nodos

    public int llavesSuperiores() {
        return orden - 1;
    } // Fin Llaves Superiores

    public int llavesInferiores() {
        return Math.max((orden / 2) - 1, 1);
    } // Fin Llaves Inferiores
    
    public NodoIndice B_Tree_Search(int index_node, String llave_primaria) {
        int num = 0;
        Node nodo = nodos.get((int) index_node);
        while (num < nodo.getN() && llave_primaria.compareTo(nodo.getLlaves().get(num).getLlave()) > 0) {
            num++;
        } // Fin While
        if (num < nodo.getN() && llave_primaria.compareTo(nodo.getLlaves().get(num).getLlave()) == 0) {
            return new NodoIndice(nodo, num);
        } // Fin If
        if (nodo.isLeaf()) {
            return null;
        } else {
            return B_Tree_Search(nodo.getHijos().get(num), llave_primaria);
        } // Fin If
    } // Fin BTree Search

    public void searchByAffinity(int ix, String k, ArrayList<Long> rrns) {
        int i = 0;
        Node x = nodos.get((int) ix);
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
            i++;
        } // Fin While
        while (i < x.getN() && k.compareTo(x.getLlaves().get(i).getLlave()) == 0) {
            rrns.add(x.getLlaves().get(i).getPos());
            if (!x.isLeaf()) {
                searchByAffinity(x.getHijos().get(i), k, rrns);
            } // Fin If
            i++;
        } // Fin While
        if (!x.isLeaf()) {
            searchByAffinity(x.getHijos().get(i), k, rrns);
        } // Fin If
    } // Fin Search By Affinity

    public void B_Tree_Insert(String k, long p) {
        int ir = raiz;
        Node r = nodos.get(this.getRaiz());
        //System.out.println(r.getLlaves().size());
        //System.out.println(raiz.getLlaves().size());
        if (r.getN() == llavesSuperiores()) {
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
        } // Fin If
    } // Fin BTree Insert

    public void B_Tree_Split_Child(int ix, int i, int iy) {
        int iz = nodos.size();
        Node z = new Node(orden);
        nodos.add(z);
        Node y = nodos.get(iy);
        Node x = nodos.get(ix);
        z.setLeaf(y.isLeaf());
        z.setN(this.llavesSuperiores() - this.llavesInferiores() - 1);
        for (int j = 0; j < z.getN(); j++) {
            z.getLlaves().set(j, y.getLlaves().get(j + this.llavesInferiores() + 1));
        } // Fin For
        if (!y.isLeaf()) {
            for (int j = 0; j < z.getN() + 1; j++) {
                z.getHijos().set(j, y.getHijos().get(j + this.llavesInferiores() + 1));
            } // Fin For
        } // Fin If
        y.setN(this.llavesInferiores());
        x.getHijos().add(i + 1, iz);
        x.getHijos().remove(orden);
        x.getLlaves().add(i, y.getLlaves().get(this.llavesInferiores()));
        x.getLlaves().remove(this.llavesSuperiores());
        x.setN(x.getN() + 1);
    } // Fin B Tree Split Child

    public void B_Tree_Insert_NonFull(int ix, String k, long p) {
        Node x = nodos.get(ix);
        int i = x.getN() - 1;
        if (x.isLeaf()) {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            } // Fin While
            x.getLlaves().add(i + 1, new LlavePos(k, p));
            x.getLlaves().remove(this.llavesSuperiores());
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && k.compareTo(x.getLlaves().get(i).getLlave()) < 0) {
                i--;
            } // File While
            i++;

            if (nodos.get(x.getHijos().get(i)).getN() == this.llavesSuperiores()) {
                B_Tree_Split_Child(ix, i, x.getHijos().get(i));
                if (k.compareTo(x.getLlaves().get(i).getLlave()) > 0) {
                    i++;
                } // Fin If
            } // Fin If
            B_Tree_Insert_NonFull(x.getHijos().get(i), k, p);
        } // Fin If
    } // Fin BTree Insert NonFull

    public void B_Tree_Delete_Key(int ix, String key) {
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
                    } // Fin If
                } // Fin For
                x.setN(x.getN() - 1);
                return;
            } // Fin If
            if (!x.isLeaf()) {
                int ipred = x.getHijos().get(pos);
                Node prednode = nodos.get(ipred);
                String pred_llave = "";
                long pos_pred = -1;
                if (prednode.getN() >= llavesInferiores() + 1) {
                    for (;;) {
                        if (prednode.isLeaf()) {
                            pred_llave = prednode.getLlaves().get(prednode.getN() - 1).getLlave();
                            pos_pred = prednode.getLlaves().get(prednode.getN() - 1).getPos();
                            break;
                        } else {
                            ipred = prednode.getHijos().get(prednode.getN());
                            prednode = nodos.get(ipred);
                        } // Fin If
                    } // Fin For
                    B_Tree_Delete_Key(ix, key);
                    x.getLlaves().set(pos, new LlavePos(pred_llave, pos_pred));
                    return;
                } // Fin If
                int inext = x.getHijos().get(pos + 1);
                Node nextNode = nodos.get(inext);
                if (nextNode.n >= llavesInferiores() + 1) {
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
                            } // Fin If
                        } // Fin For
                    } // Fin If
                    B_Tree_Delete_Key(inext, next_llave);
                    x.getLlaves().set(pos, new LlavePos(next_llave, next_pos));
                    return;
                } // Fin If
                int temp = prednode.getN() + 1;
                prednode.getLlaves().set(prednode.n++, x.getLlaves().get(pos));
                for (int i = 0, j = prednode.getN(); i < nextNode.getN(); i++) {
                    prednode.getLlaves().set(j++, nextNode.getLlaves().get(i));
                    prednode.n++;
                } // Fin For
                for (int i = 0; i < nextNode.n + 1; i++) {
                    prednode.getHijos().set(temp++, nextNode.getHijos().get(i));
                } // Fin For
                x.getHijos().set(pos, ipred);
                for (int i = pos; i < x.n; i++) {
                    if (i != orden - 2) {
                        x.getLlaves().set(i, x.getLlaves().get(i + 1));
                    } // Fin If
                } // Fin For
                for (int i = pos + 1; i < x.n + 1; i++) {
                    if (i != orden - 1) {
                        x.getHijos().set(i, x.getHijos().get(i + 1));
                    } // Fin If
                } // Fin For
                x.n--;
                if (x.n == 0) {
                    if (ix == raiz) {
                        raiz = x.getHijos().get(0);
                    } // Fin If
                    ix = x.getHijos().get(0);
                    x = nodos.get(ix);
                } // Fin If
                B_Tree_Delete_Key(ipred, key);
                return;
            } // Fin If
        } else {
            for (pos = 0; pos < x.n; pos++) {
                if (x.getLlaves().get(pos).getLlave().compareTo(key) > 0) {
                    break;
                } // Fin If
            }//fin for
            int i_temp = x.getHijos().get(pos);
            Node temporal = nodos.get(i_temp);
            if (temporal.n >= llavesInferiores() + 1) {
                B_Tree_Delete_Key(i_temp, key);
                return;
            }//fn if
            if (true) {
                int inb = -1;
                Node nb = null;
                String divide = "";
                long dividePos = -1;

                if (pos != x.n && nodos.get(x.getHijos().get(pos + 1)).n >= llavesInferiores() + 1) {
                    divide = x.getLlaves().get(pos).getLlave();
                    dividePos = x.getLlaves().get(pos).getPos();
                    inb = x.getHijos().get(pos + 1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos, nb.getLlaves().get(0));
                    temporal.getLlaves().set(temporal.n++, new LlavePos(divide, dividePos));
                    temporal.getHijos().set(temporal.n, nb.getHijos().get(0));
                    for (int i = 1; i < nb.n; i++) {
                        nb.getLlaves().set(i - 1, nb.getLlaves().get(i));
                    } // Fin For
                    for (int i = 1; i <= nb.n; i++) {
                        nb.getHijos().set(i - 1, nb.getHijos().get(i));
                    } // Fin For
                    nb.n--;
                    B_Tree_Delete_Key(i_temp, key);
                    return;
                } else if (pos != 0 && nodos.get(x.getHijos().get(pos - 1)).n >= llavesInferiores() + 1) {
                    divide = x.getLlaves().get(pos - 1).getLlave();
                    dividePos = x.getLlaves().get(pos - 1).getPos();
                    inb = x.getHijos().get(pos - 1);
                    nb = nodos.get(inb);
                    x.getLlaves().set(pos - 1, nb.getLlaves().get(nb.n - 1));
                    int ichild = nb.getHijos().get(nb.n);
                    nb.n--;
                    for (int i = temporal.n; i > 0; i--) {
                        temporal.getLlaves().set(i, temporal.getLlaves().get(i - 1));
                    } // Fin For
                    temporal.getLlaves().set(0, new LlavePos(divide, dividePos));
                    for (int i = temporal.n + 1; i > 0; i--) {
                        temporal.getHijos().set(i, temporal.getHijos().get(i - 1));
                    } // Fin For
                    temporal.getHijos().set(0, ichild);
                    temporal.n++;
                    B_Tree_Delete_Key(i_temp, key);
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
                    } // Fin If
                    for (int i = pos; i < x.n - 1; i++) {
                        x.getLlaves().set(i, x.getLlaves().get(i + 1));
                    } // Fin For
                    for (int i = pos + 1; i < x.n; i++) {
                        x.getHijos().set(i, x.getHijos().get(i + 1));
                    } // Fin For
                    x.n--;
                    lt.getLlaves().set(lt.n++, new LlavePos(divide, dividePos));
                    for (int i = 0, j = lt.n; i < rt.n + 1; i++, j++) {
                        if (i < rt.n) {
                            lt.getLlaves().set(j, rt.getLlaves().get(i));
                        } // Fin If
                        lt.getHijos().set(j, rt.getHijos().get(i));
                    } // Fin For
                    lt.n += rt.n;
                    if (x.n == 0) {
                        if (ix == raiz) {
                            raiz = x.getHijos().get(0);
                        } // Fin If
                        ix = x.getHijos().get(0);
                        x = nodos.get(ix);
                    } // Fin If
                    B_Tree_Delete_Key(ilt, key);
                    return;
                } // Fin If
            } // Fin If
        } // Fin I
    } // B Tree Delete Key

    public void BTree_KeysInOrder(int IndiceNodoActual, ArrayList<Long> lista) {
        if (IndiceNodoActual >= 0 /*&& lista.size()< 5*/) {//descomentar en caso que solo se deban listar 5 registros y nada mas
            Node node = nodos.get(IndiceNodoActual);
            for (int i = 0; i < node.getN(); i++) {
                if (lista.size() < 5) {//usar este if en caso que solo se deban listar 5 registros y nada mas
                    BTree_KeysInOrder(node.getHijos().get(i), lista);
                    lista.add(node.getLlaves().get(i).getPos());
                } else {
                    i = node.getN();
                } // Fin If
            } // Fin For
            BTree_KeysInOrder(node.getHijos().get(node.getN()), lista);
        } // Fin If
    } // Fin BTree Keys In Order
} // Fin Clase BTree
