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

    private Workbook workbook;

    public void BTree_KeysInOrder(int IndiceNodoActual, ArrayList<Long> lista, BTree arbol_actual) {
        if (IndiceNodoActual >= 0) {
            Node node = arbol_actual.getNodos().get(IndiceNodoActual);
            for (int i = 0; i < node.getN(); i++) {
                BTree_KeysInOrder(node.getHijos().get(i), lista, arbol_actual);
                lista.add(node.getLlaves().get(i).getPos());
            } // Fin For
            BTree_KeysInOrder(node.getHijos().get(node.getN()), lista, arbol_actual);
        } // Fin If
    } // Fin BTree Keys In Order

    public String llenar_excel(Archivo archivo_actual, File archivo_excel, ArrayList<Long> lista) throws IOException {
        String respuesta = "No se realizo con exito la exportación.";
        if (archivo_excel.getName().endsWith("xlsx")) {
            workbook = new HSSFWorkbook();
        } else {//ver video
            workbook = new XSSFWorkbook();
        } // Fin If
        String nombre_txt = archivo_actual.getArchivo().getName();
        Sheet hoja = workbook.createSheet(nombre_txt.substring(0, nombre_txt.length() - 4));
        for (int i = -1; i < lista.size(); i++) {
            Row fila = hoja.createRow(i + 1);
            if (i == -1) {
                //AGREGA EL NOMBRE DE LOS CAMPOS
                for (int j = 0; j < archivo_actual.getCampos().size(); j++) {
                    //SE CREA UNA NUEVA CELDA EN LA POSICION J DE LA FILA I
                    Cell celda = fila.createCell(j);
                    //SE LE AGREGA LA INFORMACION A LA CELDA
                    celda.setCellValue(archivo_actual.getCampos().get(j).getNombre());
                } // Fin For
            } else {
                //AGREGA LOS REGISTROS AL EXCEL
                long RRN = lista.get(i);
                String line = leerRegistro(Math.toIntExact(RRN), archivo_actual);
                //LE HAGO UN .SPLIT A LOS REGISTROS
                String arreglo[] = line.split("\\|");
                //EL FOR DE J ES PARA PODER RECORRER LA CANTIDAD DE COLUMNAS
                for (int j = 0; j < archivo_actual.getCampos().size(); j++) {
                    //SE CREA UNA NUEVA CELDA EN LA POSICION J DE LA FILA I
                    Cell celda = fila.createCell(j);
                    String insertar = arreglo[j];
                    celda.setCellValue(insertar);
                } // Fin For
            } // Fin If
            workbook.write(new FileOutputStream(archivo_excel));
            respuesta = "¡Exportación Completada!";
        } // Fin If
        return respuesta;
    } // Fin Llenar Excel

    private String leerRegistro(int RRN, Archivo archivo_actual) {
        String linea = "";
        // Esto lo que hace es asegurarse de leer el archivo correcto
        File archivo = new File(archivo_actual.getArchivo().getAbsolutePath());
        try {
            RandomAccessFile af = new RandomAccessFile(archivo, "rw");
            af.seek(RRN);
            for (int i = 0; i < archivo_actual.LongitudFijaCampos(); i++) {
                linea += af.readChar();
            } // Fin For
        } catch (IOException e) {
        } // Fin Try
        return linea;
    } // Fin Leer Registro

} // Fin Clase Excel Modelo
