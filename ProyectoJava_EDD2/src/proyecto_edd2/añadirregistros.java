/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author diego
 */
public class añadirregistros {

    private static RandomAccessFile flujo;
    private static int numeroregistros;
    private static int tamregistro = 50;

    public static void crearfile(File archivo) throws IOException {
        if (archivo.exists() && !archivo.isFile()) {
            throw new IOException(archivo.getName() + "No es un archivo");
        }
        flujo = new RandomAccessFile(archivo, "rw");
        numeroregistros = (int) Math.ceil((double) flujo.length() / (double) tamregistro);
    }

    public static void cerrar() throws IOException {
        flujo.close();
    }

    public static boolean setregistro(int i, Registro registro) throws IOException {
        if (i >= 0 && i <= getnumeroregistros()) {
            if (registro.getTamaño() > tamregistro) {
                System.out.println("El tamaño del registro se escedio");
            } else {
                flujo.seek(i * tamregistro);
                flujo.writeUTF(registro.getLlave());
                flujo.writeUTF(registro.getResto());
                return true;
            }
        } else {
            System.out.println("numero del registro esta fuera de limites");
        }
        return false;
    }

    public static void addregistro(Registro registro) throws IOException {
        if (setregistro(numeroregistros, registro)) {
            numeroregistros++;
        }
    }

    public static int getnumeroregistros() {
        return numeroregistros;
    }

    public static Registro getregistro(int i) throws IOException {
        if (i >= 0 && i <= getnumeroregistros()) {
            flujo.seek(i * tamregistro);
            return new Registro(flujo.readUTF(),flujo.readUTF());
        } else {
            System.out.println("numero de registro fuera del limite");
            return null;
        }
    }

    public static int buscarregistro(String buscado) throws IOException {
        String nombre;
        if (buscado == null) {
            return -1;
        }
        for (int i = 0; i < getnumeroregistros(); i++) {
            flujo.seek(i * tamregistro);
            nombre = getregistro(i).getLlave();
            if(nombre.equals(buscado)){
                return i;
            }
        }
        return -1;
    }
}
