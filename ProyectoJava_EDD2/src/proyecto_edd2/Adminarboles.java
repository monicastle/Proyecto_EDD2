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
 * @author diego
 */
public class Adminarboles {
    //esta clase me sirve de admin para los arboles creados y que no se borren
    //esta clase se basa en administrar archivos
      private ArrayList<Archivoconarbolb> listaarboles  = new ArrayList();
    private File archivo = null;
    Random random = new Random();
    private ArbolB arbol;

    public Adminarboles(String path) {
          archivo = new File(path);
    }

    public ArrayList<Archivoconarbolb> getListaarboles() {
        return listaarboles;
    }

   

    public void setArbol(int orden) {
        this.arbol = new ArbolB(orden);
    }

    public ArbolB getArbol() {
        return arbol;
    }

    public void setListaarboles(ArrayList<Archivoconarbolb> listaarboles) {
        this.listaarboles = listaarboles;
    }


    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
     public void cargarArchivo() {
        try {
            listaarboles = new ArrayList();
            Archivoconarbolb arch;
            if (archivo.exists()) {
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try {
                    while ((arch = (Archivoconarbolb) objeto.readObject()) != null) {
                        listaarboles.add(arch);
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
            for (Archivoconarbolb a : listaarboles) {
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
            for (Archivoconarbolb listaarboles : listaarboles) {
                if (listaarboles.getID() == ran) {
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
