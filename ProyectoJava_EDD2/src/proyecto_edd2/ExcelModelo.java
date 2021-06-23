/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_edd2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Onasis Reyes
 */
public class ExcelModelo {

    private Workbook wb;

    public void BTree_KeysInOrder(int IndiceNodoActual, ArrayList<Long> lista, BTree arbol_actual) {
        if (IndiceNodoActual >= 0) {
            Node node = arbol_actual.getNodos().get(IndiceNodoActual);
            for (int i = 0; i < node.getN(); i++) {
                BTree_KeysInOrder(node.getHijos().get(i), lista, arbol_actual);
                lista.add(node.getLlaves().get(i).getPos());
            }
            BTree_KeysInOrder(node.getHijos().get(node.getN()), lista, arbol_actual);
        }//fin if
    }//fin método

    public String llenar_excel(Archivo archivo_actual, File archivo_excel, ArrayList<Long> lista) throws IOException {
        String respuesta = "No se realizo con exito la exportación.";
        //REVISAR EL VIDEO PARA EVR QUE ES ESTO
        if (archivo_excel.getName().endsWith("xlsx")) {//ver video
            wb = new HSSFWorkbook();
        } else {//ver video
            wb = new XSSFWorkbook();
        }//*/
        String nombre_txt = archivo_actual.getArchivo().getName();
        Sheet hoja = wb.createSheet(nombre_txt.substring(0, nombre_txt.length() - 4));
        System.out.println("size lista " + lista.size());
        for (int i = -1; i < 5000; i++) {
            Row fila = hoja.createRow(i + 1);
            if (i == -1) {
                //AGREGA EL NOMBRE DE LOS CAMPOS
                for (int j = 0; j < archivo_actual.getCampos().size(); j++) {
                    //SE CREA UNA NUEVA CELDA EN LA POSICION J DE LA FILA I
                    Cell celda = fila.createCell(j);
                    //SE LE AGREGA LA INFORMACION A LA CELDA
                    celda.setCellValue(archivo_actual.getCampos().get(j).getNombre());
                }//fin for
            } else {
                //AGREGA LOS REGISTROS AL EXCEL
                long RRN = lista.get(i);
                String data = leer_registro(Math.toIntExact(RRN), archivo_actual);
                //LE HAGO UN .SPLIT A LOS REGISTROS
                String arr[] = data.split("\\|");
                //EL FOR DE J ES PARA PODER RECORRER LA CANTIDAD DE COLUMNAS
                for (int j = 0; j < archivo_actual.getCampos().size(); j++) {
                    //SE CREA UNA NUEVA CELDA EN LA POSICION J DE LA FILA I
                    Cell celda = fila.createCell(j);
                    String insertar = arr[j];
                    celda.setCellValue(insertar);
                    wb.write(new FileOutputStream(archivo_excel));
                }//fin for*/
            }
            respuesta = "Exportación Completa";
        }//fin else
        return respuesta;
    }

    private String leer_registro(int RRN, Archivo archivo_actual) {
        String linea = "";
        File archivo = new File(archivo_actual.getArchivo().getAbsolutePath());//esto lo que hace es asegurarse de leer el archivo correcto
        try {
            RandomAccessFile af = new RandomAccessFile(archivo, "rw");
            af.seek(RRN);
            for (int i = 0; i < archivo_actual.longitud_fija_campos(); i++) {
                linea += af.readChar();
            }
            System.out.println("line " + linea);
        } catch (IOException e) {
            System.out.println("exception -> exportacion excel -> leer registros");
        }
        return linea;
    }//fin metodo

    public int getRrn(Archivo archivo_actual) {
        Administrar_Archivos aa = new Administrar_Archivos("./Archivos.dmo");
        aa.cargarArchivo();
        if (archivo_actual.getAvailList().isEmpty()) {
            int pos_archivo = 0;
            for (int i = 0; i < aa.getLista_archivos().size(); i++) {
                if (aa.getLista_archivos().get(i).getID() == archivo_actual.getID()) {
                    pos_archivo = i;
                    break;
                } // Fin If
            } // Fin For
            if (aa.getLista_archivos().get(pos_archivo).getCant_regisros() == 0) {
                aa.cargarArchivo();
                int rrn = 500;
                aa.getLista_archivos().get(pos_archivo).setCant_regisros(true);
                aa.escribirArchivo();
                return rrn;
            } else {
                aa.cargarArchivo();
                int rrn = (250 + (archivo_actual.longitud_fija_campos() * aa.getLista_archivos().get(pos_archivo).getCant_regisros())) * 2;
                aa.getLista_archivos().get(pos_archivo).setCant_regisros(true);
                aa.escribirArchivo();
                return rrn;
            } // Fin If
        } // Fin If
        return (int) archivo_actual.getAvailList().peekFirst();
    } // Fin If
    
}//fin clase
