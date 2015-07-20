package com.arkamovil.android.procesos;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.lowagie.text.Image;

import java.util.ArrayList;
import java.util.List;

public class TablaInventarios {

    private double f1 = 0.25;
    private double f2 = 0.4;
    private double f3 = 0.25;
    private double f4 = 0.05;

    private static TableLayout tabla;
    private static TableLayout cabecera;

    private static TableRow.LayoutParams layoutFila;
    private static TableRow.LayoutParams layoutId;
    private static TableRow.LayoutParams layoutTexto;
    private static TableRow.LayoutParams layoutFuncionario;
    private static TableRow.LayoutParams layoutMod;

    private static Activity actividad;
    private static View vista;

    private boolean selectAll;

    private static final int factor = 5;


    private static List<String> nom_fun;
    private static List<String> doc_fun;
    private static List<String> sede;
    private static List<String> dependencia;
    private static List<String> ubicacion;

    private static int inicio;

    private static int tamanoPantalla;


    private static int MAX_FILAS = 0;


    public void crear(View rootView, Activity actividad, List<String> doc_fun, List<String> sede, List<String> dependencia, List<String> ubicacion) {

        this.actividad = actividad;
        this.vista = rootView;

        this.doc_fun = doc_fun;
        this.sede = sede;
        this.dependencia = dependencia;
        this.ubicacion = ubicacion;

        this.tamanoPantalla = rootView.getWidth();

        if (doc_fun.size() < this.factor) {
            this.MAX_FILAS = doc_fun.size();
        } else {
            this.MAX_FILAS = this.factor;
        }

        this.inicio = 0;

        cargarElementos();

        if (doc_fun.size() > 0) {
            agregarCabecera();

            agregarFilasTabla();
        } else {
            Toast.makeText(actividad, "No existen elementos disponibles en este momento para ser asignados.", Toast.LENGTH_LONG).show();
        }


    }

    public void agregarCabecera() {

        TableRow fila;
        TextView txtNombreFuncionario;
        TextView txtDocumentoFuncionario;
        TextView txtDependencia;
        TextView txtInformacion;


        fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        txtNombreFuncionario = new TextView(actividad);
        txtDocumentoFuncionario = new TextView(actividad);
        txtDependencia = new TextView(actividad);
        txtInformacion = new TextView(actividad);

        txtNombreFuncionario.setText("Nombre");
        txtNombreFuncionario.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNombreFuncionario.setTextAppearance(actividad, R.style.etiqueta);
        txtNombreFuncionario.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtNombreFuncionario.setLayoutParams(layoutId);

        txtDocumentoFuncionario.setText("Documento");
        txtDocumentoFuncionario.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDocumentoFuncionario.setTextAppearance(actividad, R.style.etiqueta);
        txtDocumentoFuncionario.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDocumentoFuncionario.setLayoutParams(layoutTexto);

        txtDependencia.setText("Ubicación");
        txtDependencia.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDependencia.setTextAppearance(actividad, R.style.etiqueta);
        txtDependencia.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDependencia.setLayoutParams(layoutFuncionario);

        txtInformacion.setText("Ver Inventario");
        txtInformacion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInformacion.setTextAppearance(actividad, R.style.etiqueta);
        txtInformacion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtInformacion.setLayoutParams(layoutMod);

        fila.addView(txtNombreFuncionario);
        fila.addView(txtDocumentoFuncionario);
        fila.addView(txtDependencia);
        fila.addView(txtInformacion);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla() {

        TableRow fila;
        TextView nombre;
        TextView documento;
        TextView dependencia;
        ImageView verInventario;

        for (int i = 0; i < MAX_FILAS; i++) {
            fila = new TableRow(actividad);
            fila.setLayoutParams(layoutFila);

            nombre = new TextView(actividad);
            documento = new TextView(actividad);
            dependencia = new TextView(actividad);
            verInventario = new ImageView(actividad);

            nombre.setText(doc_fun.get(inicio + i));
            nombre.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            nombre.setTextAppearance(actividad, R.style.etiqueta);
            nombre.setBackgroundResource(R.drawable.tabla_celda);
            nombre.setLayoutParams(layoutId);

            documento.setText(doc_fun.get(inicio + i));
            documento.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            documento.setTextAppearance(actividad, R.style.etiqueta);
            documento.setBackgroundResource(R.drawable.tabla_celda);
            documento.setLayoutParams(layoutTexto);

            dependencia.setText(sede.get(inicio + i));
            dependencia.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            dependencia.setTextAppearance(actividad, R.style.etiqueta);
            dependencia.setBackgroundResource(R.drawable.tabla_celda);
            dependencia.setLayoutParams(layoutFuncionario);

            verInventario.setId(inicio + i);
            verInventario.setBackgroundResource(R.drawable.tabla_celda);
            verInventario.setLayoutParams(layoutMod);

            fila.addView(nombre);
            fila.addView(documento);
            fila.addView(dependencia);
            fila.addView(verInventario);

            tabla.addView(fila);

        }
    }

    public void cargarElementos() {

        tabla.removeAllViews();

        tabla = (TableLayout) vista.findViewById(R.id.tabla_c2);
        cabecera = (TableLayout) vista.findViewById(R.id.cabecera_c2);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla * f1), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla * f2), TableRow.LayoutParams.MATCH_PARENT);
        layoutFuncionario = new TableRow.LayoutParams((int) (tamanoPantalla * f3), TableRow.LayoutParams.MATCH_PARENT);
        layoutMod = new TableRow.LayoutParams((int) (tamanoPantalla * f4), TableRow.LayoutParams.MATCH_PARENT);

    }

    public void bajar(View rootView, Activity actividad) {

        if (this.inicio <= (doc_fun.size() - (factor + 1))) {
            cargarElementos();
            this.inicio++;
            agregarFilasTabla();
        }
    }

    public void subir(View rootView, Activity actividad) {

        if (this.inicio > 0) {
            cargarElementos();
            this.inicio--;
            agregarFilasTabla();
        }
    }

    public void borrarTabla(View rootView, Activity actividad) {


        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c2);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c2);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla * f1), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla * f2), TableRow.LayoutParams.MATCH_PARENT);
        layoutFuncionario = new TableRow.LayoutParams((int) (tamanoPantalla * f3), TableRow.LayoutParams.MATCH_PARENT);
        layoutMod = new TableRow.LayoutParams((int) (tamanoPantalla * f4), TableRow.LayoutParams.MATCH_PARENT);


        tabla.removeAllViews();
        cabecera.removeAllViews();
    }
}
