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
import java.util.ArrayList;

/**
 *
 * @author Onasis Reyes
 */
public class administrar_campos {

    private ArrayList<Campo> lista_campos = new ArrayList();
    private File archivo = null;

    public administrar_campos() {
    }

    public administrar_campos(String path) {
        archivo = new File(path);
    }

    public ArrayList<Campo> getCampos() {
        return lista_campos;
    }

    public void añadirCampo(Campo campo) {
        lista_campos.add(campo);
    }

    public void setCampos(ArrayList<Campo> lista_campos) {
        this.lista_campos = lista_campos;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public void cargarArchivo() {
        lista_campos = new ArrayList();
        Campo campo;
        try {
            if (archivo.exists()) {
                FileInputStream entrada
                        = new FileInputStream(archivo);
                ObjectInputStream objeto
                        = new ObjectInputStream(entrada);
                try {
                    while ((campo = (Campo) objeto.readObject()) != null) {
                        lista_campos.add(campo);
                    }
                } catch (EOFException e) {
                    System.out.println("stacktrace while");
                }
                objeto.close();
                entrada.close();
            }
        } catch (Exception ex) {
            System.out.println("stacktrace");
           // ex.printStackTrace();
        }
    }

    public void escribirArchivo() {
        FileOutputStream fw = null;
        ObjectOutputStream bw = null;
        try {
            fw = new FileOutputStream(archivo);
            bw = new ObjectOutputStream(fw);
            for (Campo user : lista_campos) {
                bw.writeObject(user);
            }
            bw.flush();
        } catch (IOException ex) {
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
            }
        }
    }// Fin Metodo

}// Fin Clase
