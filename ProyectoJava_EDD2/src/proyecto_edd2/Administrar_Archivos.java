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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Monica
 */
public class Administrar_Archivos {

    private ArrayList<Archivo> lista_archivos = new ArrayList();
    private File archivo = null;
    Random random = new Random();

    public Administrar_Archivos(String path) {
        archivo = new File(path);
    } // Fin Constructor Administrar Archivos

    public ArrayList<Archivo> getLista_archivos() {
        return lista_archivos;
    } // Fin Get Lista Archivos

    public void setLista_archivos(ArrayList<Archivo> lista_archivos) {
        this.lista_archivos = lista_archivos;
    } // Fin Set Lista Archivos

    public File getArchivo() {
        return archivo;
    } // Fin Get Archivo

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    } // Fin Set Archivo

    public void AddArchivo(Archivo ar) {
        this.lista_archivos.add(ar);
    } // Fin Set Proyecto

    public void RemoveArchivo(Archivo ar) {
        this.lista_archivos.remove(ar);
    } // Fin Remove Proyecto

    public void cargarArchivo() {
        try {
            lista_archivos = new ArrayList();
            Archivo arch;
            if (archivo.exists()) {
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try {
                    while ((arch = (Archivo) objeto.readObject()) != null) {
                        lista_archivos.add(arch);
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
            for (Archivo a : lista_archivos) {
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
            for (Archivo listaarchivo : lista_archivos) {
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

} // Fin Clase Administrar Archivos
