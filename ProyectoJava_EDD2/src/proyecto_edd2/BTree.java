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
        Node xnode = nodos.get((int) ix);
        while (i < xnode.getN() && k.compareTo(xnode.getLlaves().get(i).getLlave()) > 0) {
            i++;
        } // Fin While
        while (i < xnode.getN() && k.compareTo(xnode.getLlaves().get(i).getLlave()) == 0) {
            rrns.add(xnode.getLlaves().get(i).getPos());
            if (!xnode.isLeaf()) {
                searchByAffinity(xnode.getHijos().get(i), k, rrns);
            } // Fin If
            i++;
        } // Fin While
        if (!xnode.isLeaf()) {
            searchByAffinity(xnode.getHijos().get(i), k, rrns);
        } // Fin If
    } // Fin Search By Affinity

    public void B_Tree_Insert(String key, long p) {
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
            B_Tree_Insert_NonFull(is, key, p);
        } else {
            B_Tree_Insert_NonFull(ir, key, p);
        } // Fin If
    } // Fin BTree Insert

    public void B_Tree_Split_Child(int indexx, int i, int indexy) {
        int iz = nodos.size();
        Node znode = new Node(orden);
        nodos.add(znode);
        Node ynode = nodos.get(indexy);
        Node xnode = nodos.get(indexx);
        znode.setLeaf(ynode.isLeaf());
        znode.setN(this.llavesSuperiores() - this.llavesInferiores() - 1);
        for (int j = 0; j < znode.getN(); j++) {
            znode.getLlaves().set(j, ynode.getLlaves().get(j + this.llavesInferiores() + 1));
        } // Fin For
        if (!ynode.isLeaf()) {
            for (int j = 0; j < znode.getN() + 1; j++) {
                znode.getHijos().set(j, ynode.getHijos().get(j + this.llavesInferiores() + 1));
            } // Fin For
        } // Fin If
        ynode.setN(this.llavesInferiores());
        xnode.getHijos().add(i + 1, iz);
        xnode.getHijos().remove(orden);
        xnode.getLlaves().add(i, ynode.getLlaves().get(this.llavesInferiores()));
        xnode.getLlaves().remove(this.llavesSuperiores());
        xnode.setN(xnode.getN() + 1);
    } // Fin B Tree Split Child

    public void B_Tree_Insert_NonFull(int r, String key, long p) {
        Node xnode = nodos.get(r);
        int i = xnode.getN() - 1;
        if (xnode.isLeaf()) {
            while (i >= 0 && key.compareTo(xnode.getLlaves().get(i).getLlave()) < 0) {
                i--;
            } // Fin While
            xnode.getLlaves().add(i + 1, new LlavePos(key, p));
            xnode.getLlaves().remove(this.llavesSuperiores());
            xnode.setN(xnode.getN() + 1);
        } else {
            while (i >= 0 && key.compareTo(xnode.getLlaves().get(i).getLlave()) < 0) {
                i--;
            } // File While
            i++;

            if (nodos.get(xnode.getHijos().get(i)).getN() == this.llavesSuperiores()) {
                B_Tree_Split_Child(r, i, xnode.getHijos().get(i));
                if (key.compareTo(xnode.getLlaves().get(i).getLlave()) > 0) {
                    i++;
                } // Fin If
            } // Fin If
            B_Tree_Insert_NonFull(xnode.getHijos().get(i), key, p);
        } // Fin If
    } // Fin BTree Insert NonFull

    public void B_Tree_Delete_Key(int indexx, String key) {
        Node xnode = nodos.get(indexx);
        int pos = xnode.Find(key);
        if (pos != -1) {
            if (xnode.isLeaf()) {
                int i = 0;
                for (i = 0; i < xnode.getN() && xnode.getLlaves().get(i).getLlave().compareTo(key) != 0; i++) {
                }
                for (; i < xnode.getN(); i++) {
                    if (i != orden - 2) {
                        xnode.getLlaves().set(i, xnode.getLlaves().get(i + 1));
                    } // Fin If
                } // Fin For
                xnode.setN(xnode.getN() - 1);
                return;
            } // Fin If
            if (!xnode.isLeaf()) {
                int ipred = xnode.getHijos().get(pos);
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
                    B_Tree_Delete_Key(indexx, key);
                    xnode.getLlaves().set(pos, new LlavePos(pred_llave, pos_pred));
                    return;
                } // Fin If
                int inext = xnode.getHijos().get(pos + 1);
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
                    xnode.getLlaves().set(pos, new LlavePos(next_llave, next_pos));
                    return;
                } // Fin If
                int temp = prednode.getN() + 1;
                prednode.getLlaves().set(prednode.n++, xnode.getLlaves().get(pos));
                for (int i = 0, j = prednode.getN(); i < nextNode.getN(); i++) {
                    prednode.getLlaves().set(j++, nextNode.getLlaves().get(i));
                    prednode.n++;
                } // Fin For
                for (int i = 0; i < nextNode.n + 1; i++) {
                    prednode.getHijos().set(temp++, nextNode.getHijos().get(i));
                } // Fin For
                xnode.getHijos().set(pos, ipred);
                for (int i = pos; i < xnode.n; i++) {
                    if (i != orden - 2) {
                        xnode.getLlaves().set(i, xnode.getLlaves().get(i + 1));
                    } // Fin If
                } // Fin For
                for (int i = pos + 1; i < xnode.n + 1; i++) {
                    if (i != orden - 1) {
                        xnode.getHijos().set(i, xnode.getHijos().get(i + 1));
                    } // Fin If
                } // Fin For
                xnode.n--;
                if (xnode.n == 0) {
                    if (indexx == raiz) {
                        raiz = xnode.getHijos().get(0);
                    } // Fin If
                    indexx = xnode.getHijos().get(0);
                    xnode = nodos.get(indexx);
                } // Fin If
                B_Tree_Delete_Key(ipred, key);
                return;
            } // Fin If
        } else {
            for (pos = 0; pos < xnode.n; pos++) {
                if (xnode.getLlaves().get(pos).getLlave().compareTo(key) > 0) {
                    break;
                } // Fin If
            }//fin for
            int i_temp = xnode.getHijos().get(pos);
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

                if (pos != xnode.n && nodos.get(xnode.getHijos().get(pos + 1)).n >= llavesInferiores() + 1) {
                    divide = xnode.getLlaves().get(pos).getLlave();
                    dividePos = xnode.getLlaves().get(pos).getPos();
                    inb = xnode.getHijos().get(pos + 1);
                    nb = nodos.get(inb);
                    xnode.getLlaves().set(pos, nb.getLlaves().get(0));
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
                } else if (pos != 0 && nodos.get(xnode.getHijos().get(pos - 1)).n >= llavesInferiores() + 1) {
                    divide = xnode.getLlaves().get(pos - 1).getLlave();
                    dividePos = xnode.getLlaves().get(pos - 1).getPos();
                    inb = xnode.getHijos().get(pos - 1);
                    nb = nodos.get(inb);
                    xnode.getLlaves().set(pos - 1, nb.getLlaves().get(nb.n - 1));
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
                    int indexlt = -1;
                    Node lefttemp = null;
                    int indexrt = -1;
                    Node righttemp = null;
                    boolean last = false;
                    if (pos != xnode.n) {
                        divide = xnode.getLlaves().get(pos).getLlave();
                        dividePos = xnode.getLlaves().get(pos).getPos();
                        indexlt = xnode.getHijos().get(pos);
                        lefttemp = nodos.get(indexlt);
                        indexrt = xnode.getHijos().get(pos + 1);
                        righttemp = nodos.get(indexrt);
                    } else {
                        divide = xnode.getLlaves().get(pos - 1).getLlave();
                        dividePos = xnode.getLlaves().get(pos - 1).getPos();
                        indexrt = xnode.getHijos().get(pos);
                        righttemp = nodos.get(indexrt);
                        indexlt = xnode.getHijos().get(pos - 1);
                        lefttemp = nodos.get(indexlt);
                        last = true;
                        pos--;
                    } // Fin If
                    for (int i = pos; i < xnode.n - 1; i++) {
                        xnode.getLlaves().set(i, xnode.getLlaves().get(i + 1));
                    } // Fin For
                    for (int i = pos + 1; i < xnode.n; i++) {
                        xnode.getHijos().set(i, xnode.getHijos().get(i + 1));
                    } // Fin For
                    xnode.n--;
                    lefttemp.getLlaves().set(lefttemp.n++, new LlavePos(divide, dividePos));
                    for (int i = 0, j = lefttemp.n; i < righttemp.n + 1; i++, j++) {
                        if (i < righttemp.n) {
                            lefttemp.getLlaves().set(j, righttemp.getLlaves().get(i));
                        } // Fin If
                        lefttemp.getHijos().set(j, righttemp.getHijos().get(i));
                    } // Fin For
                    lefttemp.n += righttemp.n;
                    if (xnode.n == 0) {
                        if (indexx == raiz) {
                            raiz = xnode.getHijos().get(0);
                        } // Fin If
                        indexx = xnode.getHijos().get(0);
                        xnode = nodos.get(indexx);
                    } // Fin If
                    B_Tree_Delete_Key(indexlt, key);
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
