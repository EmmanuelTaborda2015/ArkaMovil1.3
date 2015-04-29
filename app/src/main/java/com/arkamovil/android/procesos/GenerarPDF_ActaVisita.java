package com.arkamovil.android.procesos;


import harmony.java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.arkamovil.android.R;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GenerarPDF_ActaVisita{

    private final static String NOMBRE_DIRECTORIO = "Acta de Visita";
    private final static String NOMBRE_DOCUMENTO = "prueba.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";


    public  void generar(Resources resources){
        // Creamos el documento.
        Document documento = new Document();

        try {

            // Creamos el fichero con el nombre que deseemos.
            File f = crearFichero(NOMBRE_DOCUMENTO);

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(
                    f.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el p�e de p�gina y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "Esta es mi cabecera"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "Este es mi pie de p�gina"), false);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            // Abrimos el documento.
            documento.open();

            // se añade el titulo


            Paragraph titulo = new Paragraph("UNIVERSIDAD DISTRITAL \"FRANCISCO JOSE DE CALDAS\"");

            Font font1 = FontFactory.getFont(FontFactory.HELVETICA, 40,
                    Font.BOLD, Color.BLACK);

            titulo.setAlignment(Element.ALIGN_CENTER);

            titulo.setFont(font1);

            //documento.add(titulo);


            // se añade el subtitulo

            Paragraph subtitulo1 = new Paragraph("ALMACEN GENERAL E INVENTARIOS");


            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 38,
                    Font.BOLD, Color.BLACK);

            subtitulo1.setAlignment(Element.ALIGN_CENTER);

            subtitulo1.setFont(font2);

            //documento.add(subtitulo1);

            Paragraph subtitulo2 = new Paragraph("VISITA DE LEVANTAMIENTO FISICO DE INVENTARIOS");


            font2 = FontFactory.getFont(FontFactory.HELVETICA, 38,
                    Font.BOLD, Color.BLACK);

            subtitulo2.setAlignment(Element.ALIGN_CENTER);

            subtitulo2.setFont(font2);

            //documento.add(subtitulo2);

            // Insertamos una imagen que se encuentra en los recursos de la
            Bitmap bitmap = BitmapFactory.decodeResource(resources,
                    R.drawable.ud);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 5, stream);
            Image imagen = Image.getInstance(stream.toByteArray());

            //documento.add(imagen);

            PdfPTable tablaTitulo = new PdfPTable(2);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase(titulo));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaTitulo.addCell(cell);


            cell = new PdfPCell(new Phrase());
            cell.setImage(imagen);
            cell.setRowspan(2);
            tablaTitulo.addCell(cell);
            // we add the four remaining cells with addCell()
            tablaTitulo.addCell(subtitulo1);
            tablaTitulo.addCell(subtitulo2);

            documento.add(tablaTitulo);

            // Insertamos una tabla.
            PdfPTable tabla = new PdfPTable(1);
            for (int i = 0; i < 9; i++) {
                tabla.addCell("Celda " + i);

            }


            String sede = "Macarena";
            String facultad = "Ingenieria";
            String dependencia = "Sistemas";
            String nomres = "Emmanuel";
            String cedres = "1032418";
            String obser = "Hola esta es una prueba para ver como se desenvuelven las diferentes observaciones";
            String numvis = "1";
            String fechaDia = "15";
            String fechaMes = "04";
            String fechaAnno = "15";
            String proxvisDia = "14";
            String proxvisMes = "05";
            String proxvisAnno = "15";

            //documento.add(tabla);

            PdfPTable tablaDatos = new PdfPTable(4);

            PdfPCell Datos;
            Datos = new PdfPCell(new Phrase("FECHA"));
            Datos.setRowspan(3);
            tablaDatos.addCell(Datos);
            // we add the four remaining cells with addCell()
            tablaDatos.addCell("Dia  " + fechaDia);
            tablaDatos.addCell("Mes  " + fechaMes);
            tablaDatos.addCell("Año  " + fechaAnno);

            Datos = new PdfPCell(new Phrase("SEDE:" + "\n\n" + sede));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("FACULTAD:" + "\n\n" + facultad));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("DEPENDENCIA:" + "\n\n" + dependencia));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("NOMBRE DEL RESPONSABLE:" + "\n\n" + nomres));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("CEDULA DEL RESPONSABLE:" + "\n\n" + cedres));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("OBSERVACIONES:" + "\n\n" + obser));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("VISITA No.:" + "\n\n" + numvis));
            Datos.setColspan(4);
            Datos.setPadding(5);
            tablaDatos.addCell(Datos);

            Datos = new PdfPCell(new Phrase("PROXIMA VISITA"));
            Datos.setRowspan(4);
            tablaDatos.addCell(Datos);
            // we add the four remaining cells with addCell()
            tablaDatos.addCell("Dia  " + fechaDia);
            tablaDatos.addCell("Mes  " + fechaMes);
            tablaDatos.addCell("Año  " + fechaAnno);

            documento.add(tablaDatos);

            Datos.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaTitulo.addCell(Datos);

            // Agregar marca de agua
            Font font3 = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                    Color.GRAY);
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Paragraph(
                            "amatellanes.wordpress.com", font3), 297.5f, 421,
                    writer.getPageNumber() % 2 == 1 ? 45 : -45);

        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } finally {

            // Cerramos el documento.
            documento.close();
        }
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public static File getRuta() {

        // El fichero ser� almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }



}