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

    public String Exportar(
            File archivo,
            ArrayList<Long> lista_rrns_registros,
            BTree arbol_actual,
            Archivo archivo_actual) {
        String respuesta = "No se realizo con exito la exportación.";
        //int numFila=tablaD.getRowCount(), numColumna=tablaD.getColumnCount();
        //int pos_fila_excel = 0;
        int iterador_recursivo = -1;
        if (archivo.getName().endsWith("xlsx")) {//ver video
            System.out.println("xlsx");
            System.out.println("xlsx");
            System.out.println("xlsx");
            System.out.println("xlsx");
            wb = new HSSFWorkbook();
        } else {//ver video
            wb = new XSSFWorkbook();
        }//*/
        Sheet hoja = wb.createSheet("Registros");//para que quede con florecitas le podemos poner el nombre del file
        try {
            BTree_KeysInOrder(
                    arbol_actual.getRaiz(),
                    lista_rrns_registros,
                    hoja,
                    arbol_actual,
                    iterador_recursivo,
                    archivo_actual);//*/
            wb.write(new FileOutputStream(archivo));
            respuesta = "Exportación exitosa.";
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }//*/
        return respuesta;
    }

    public void BTree_KeysInOrder(
            int IndiceNodoActual,
            ArrayList<Long> lista,
            Sheet hoja,
            BTree arbol_actual,
            long i,
            Archivo archivo_actual) throws IOException {
        System.out.println("in");
        if (IndiceNodoActual >= 0) {
            System.out.println("btree " + i);
            //agarro el nodo actual del arbol del archivo
            Node node = arbol_actual.getNodos().get(IndiceNodoActual);
            System.out.println("paso1");
            if (i < node.getN()) {
                System.out.println("paso2");
                Row fila = hoja.createRow(Math.toIntExact(i) + 1);
                System.out.println("paso3");
                if (i == -1) {
                    System.out.println("paso4");
                    //seteo los nombres de los campos en el excel
                    for (int j = 0; j < archivo_actual.getCampos().size(); j++) {
                        Cell celda = fila.createCell(j);
                        celda.setCellValue(archivo_actual.getCampos().get(j).getNombre());
                    }//fin for
                    System.out.println("paso5");
                    //llamado recursivo
                    BTree_KeysInOrder(
                            arbol_actual.getRaiz(),
                            lista,
                            hoja,
                            arbol_actual,
                            i + 1,
                            archivo_actual);
                } else {
                    System.out.println("paso6");
                    //llamado recursivo
                    BTree_KeysInOrder(
                            node.getHijos().get(Math.toIntExact(i)),
                            lista,
                            hoja,
                            arbol_actual,
                            i + 1,
                            archivo_actual);
                    //seteo los rrns en la lista
                    System.out.println("paso7");
                    lista.add(node.getLlaves().get(Math.toIntExact(i)).getPos());
                    System.out.println("paso8");
                    //agarro el registro encontrado
                    long d = 166;
                    String data = leer_registro(Math.toIntExact(lista.get(lista.size()-1)), archivo_actual);
                    //String data = leer_registro(Math.toIntExact(lista.get(Math.toIntExact(i))), archivo_actual);
                    System.out.println("paso9");
                    //separo el registro encontrado
                    String arr[] = data.split("\\|");
                    System.out.println("paso10");
                    //agrego a la celda el registro
                    for (int k = 0; k < arr.length; k++) {
                        Cell celda = fila.createCell(k);
                        celda.setCellValue(arr[k]);
                    }// fin for
                    System.out.println("paso11");
                }//fin else
            }//fin for i
            //llamado recursivo
            BTree_KeysInOrder(
                    node.getHijos().get(node.getN()),
                    lista, hoja,
                    arbol_actual,
                    i + 1,
                    archivo_actual);
        }//fin if
        System.out.println("me voy");
    }//fin método

    private String leer_registro(int RRN, Archivo archivo_actual) {
        //System.out.println("aqui esta el RRN: " + RRN);
        //preferiblemente no tocar esta parte del cdigo a menos que les de fallos contactar al administrador
        String x = "";
        try {
            File archivo = new File(archivo_actual.getArchivo().getAbsolutePath());//esto lo que hace es asegurarse de leer el archivo correcto
            System.out.println("salio1");
            //lo de arrriba
            FileReader fr = new FileReader(archivo);
            System.out.println("salio2");
            System.out.println("salio3");
            RandomAccessFile af = new RandomAccessFile(archivo, "r");
            System.out.println("salio4");
            af.seek(RRN);//aqui es donde se se mueve de bytes para buscar la llave
            int rrn = RRN;
            System.out.println("salio4");
            //System.out.println("y este es el rrrn donde lee " + rrn);
            x = af.readLine();//esto lee la linea donde se quedo el puntero
            System.out.println("salio5");
            af.close();
            System.out.println("salio6");
            fr.close();
            System.out.println("salio7");
        } catch (Exception e) {
            System.out.println("exception -> exportacion excel -> leer registros");
        }
        return x;
    }// 

}//fin clase
