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
        for (int i = -1; i < lista.size(); i++) {
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
                    //NO TOCAR O SE MAMA
                    /*for (int k = 0; k < arr.length; k++) {
                        //LE QUITO LOS ESPACIOS INNESESARIOS
                        String insertar = arr[k].replaceAll(" ", "");
                        //SE LE AGREGA LA INFORMACION A LA CELDA
                        celda.setCellValue(insertar);
                    }//fin for k*/
                    wb.write(new FileOutputStream(archivo_excel));
                }//fin for*/
                respuesta = "Exportación Completa";
            }//fin else
        }//*/
        return respuesta;
    }

    private String leer_registro(int RRN, Archivo archivo_actual) {
        String x = "";
        try {
            File archivo = new File(archivo_actual.getArchivo().getAbsolutePath());//esto lo que hace es asegurarse de leer el archivo correcto
            FileReader fr = new FileReader(archivo);
            RandomAccessFile af = new RandomAccessFile(archivo, "r");
            af.seek(RRN);//aqui es donde se se mueve de bytes para buscar la llave
            x = af.readLine();//esto lee la linea donde se quedo el puntero
            af.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("exception -> exportacion excel -> leer registros");
        }
        return x;
    }//fin metodo 

}//fin clase
