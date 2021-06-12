/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author diego
 */
public class añadirregistros {

    private ArrayList<Registro> lista_registros = new ArrayList();
    private File archivo = null;
    Random random = new Random();

    public añadirregistros(String path) {
        archivo = new File(path);
    } // Fin Constructor Administrar Archivos

    public ArrayList<Registro> getLista_archivos() {
        return lista_registros;
    } // Fin Get Lista Archivos

    public void setLista_archivos(ArrayList<Registro> lista_registros) {
        this.lista_registros = lista_registros;
    } // Fin Set Lista Archivos

    public File getArchivo() {
        return archivo;
    } // Fin Get Archivo

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    } // Fin Set Archivo

    public void AddArchivo(Registro ar) {
        this.lista_registros.add(ar);
    } // Fin Set Proyecto

    public void RemoveArchivo(Registro ar) {
        this.lista_registros.remove(ar);
    } // Fin Remove Proyecto

    public void cargarArchivo() {
        try {
            lista_registros = new ArrayList();
            Registro arch;
            if (archivo.exists()) {
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try {
                    while ((arch = (Registro) objeto.readObject()) != null) {
                        lista_registros.add(arch);
                    } // Fin While
                } catch (EOFException ex) {
                } // Fin Try Catch
                objeto.close();
                entrada.close();
            } // Fin If
        } catch (Exception ex) {
        } // Fin Try Catch
    } // Fin Cargar Archivo

    public void escribirArchivo() {
        FileOutputStream fw = null;
        ObjectOutputStream bw = null;
        try {
            fw = new FileOutputStream(archivo);
            bw = new ObjectOutputStream(fw);
            for (Registro a : lista_registros) {
                bw.writeObject(a);
            } // Fin For
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } // Fin Try Catch
        } // Fin Try Catch
    } // Fin Escribir Archivo

    public int GenerarId() {
        // CAMBIAR
        cargarArchivo();
        boolean valid;
        while (true) {
            valid = true;
            int ran;
            ran = 1 + random.nextInt(1000);
            for (Registro listaarchivo : lista_registros) {
                if (listaarchivo.getID() == ran) {
                    valid = false;
                    break;
                } // Fin If
            } // Fin For
            if (valid) {
                return ran;
            } // Fin If
        } // Fin While
    } // Fin Generar ID

}
