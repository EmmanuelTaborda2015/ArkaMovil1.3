package com.arkamovil.android.procesos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TablaAsignarInventarios {

    private static TableLayout tabla;
    private static TableLayout cabecera;

    private static TableRow.LayoutParams layoutFila;
    private static TableRow.LayoutParams layoutId;
    private static TableRow.LayoutParams layoutTexto;
    private static TableRow.LayoutParams layoutMod;

    private static Activity actividad;
    private static View rootView;

    private static final int factor = 5;


    private static List<String> id_elemento;
    private static List<String> descripcion;
    private static boolean[] arr;


    public static boolean[] getArr() {
        return arr;
    }

    private static int inicio;

    private static int tamanoPantalla;


    private static int MAX_FILAS = 0;


    public void crear(View rootView, Activity actividad, List<String> id, List<String> desc) {

        this.actividad = actividad;
        this.rootView = rootView;

        this.tamanoPantalla = rootView.getWidth();


        this.id_elemento = id;
        this.descripcion = desc;

        if (id_elemento.size() < this.factor) {
            this.MAX_FILAS = id_elemento.size();
        } else {
            this.MAX_FILAS = this.factor;
        }

        this.inicio = 0;

        arr = new boolean[id_elemento.size()];

        for (int i = 0; i < id_elemento.size(); i++) {
            arr[i] = false;
        }

        cargarElementos();

        if (id_elemento.size() > 0) {
            agregarCabecera();

            agregarFilasTabla();
        } else {
            Toast.makeText(actividad, "No existen elementos disponibles en este momento para ser asignados.", Toast.LENGTH_LONG).show();
        }


    }

    public void agregarCabecera() {

        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;
        TextView txtInfo;


        fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(actividad);
        txtDescripcion = new TextView(actividad);
        txtInfo = new TextView(actividad);

        txtId.setText("Id");
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(actividad, R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtId.setLayoutParams(layoutId);

        txtDescripcion.setText("Descripción");
        txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDescripcion.setTextAppearance(actividad, R.style.etiqueta);
        txtDescripcion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDescripcion.setLayoutParams(layoutTexto);

        txtInfo.setText("Asignar");
        txtInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInfo.setTextAppearance(actividad, R.style.etiqueta);
        txtInfo.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtInfo.setLayoutParams(layoutMod);

        fila.addView(txtId);
        fila.addView(txtDescripcion);
        fila.addView(txtInfo);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla() {

        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;
        CheckBox txtMod;

        for (int i = 0; i < MAX_FILAS; i++) {
            fila = new TableRow(actividad);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(actividad);
            txtDescripcion = new TextView(actividad);
            txtMod = new CheckBox(actividad);

            txtId.setText(id_elemento.get(inicio + i));
            txtId.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtId.setTextAppearance(actividad, R.style.etiqueta);
            txtId.setBackgroundResource(R.drawable.tabla_celda);
            txtId.setLayoutParams(layoutId);

            txtDescripcion.setText(descripcion.get(inicio + i));
            txtDescripcion.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtDescripcion.setTextAppearance(actividad, R.style.etiqueta);
            txtDescripcion.setBackgroundResource(R.drawable.tabla_celda);
            txtDescripcion.setLayoutParams(layoutTexto);

            txtMod.setId(inicio + i);
            txtMod.setPadding(30, 30, 30, 30);
            txtMod.setBackgroundResource(R.drawable.tabla_celda);
            txtMod.setLayoutParams(layoutMod);
            txtMod.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtMod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isChecked = ((CheckBox) view).isChecked();

                    if (isChecked) {
                        arr[view.getId()] = true;
                    } else {
                        arr[view.getId()] = false;
                    }
                }
            });

            if (arr[(inicio + i)] == true) {
                txtMod.setChecked(true);
            } else {
                txtMod.setChecked(false);
            }

            fila.addView(txtId);
            fila.addView(txtDescripcion);
            fila.addView(txtMod);

            tabla.addView(fila);

        }
    }

    public void cargarElementos() {

        tabla.removeAllViews();

        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c2);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c2);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla * 0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla * 0.4), TableRow.LayoutParams.MATCH_PARENT);
        layoutMod = new TableRow.LayoutParams((int) (tamanoPantalla * 0.3), TableRow.LayoutParams.MATCH_PARENT);

    }

    public void bajar(View rootView, Activity actividad) {

        if (this.inicio <= (id_elemento.size() - (factor + 1))) {
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
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla * 0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla * 0.4), TableRow.LayoutParams.MATCH_PARENT);
        layoutMod = new TableRow.LayoutParams((int) (tamanoPantalla * 0.3), TableRow.LayoutParams.MATCH_PARENT);

        tabla.removeAllViews();
        cabecera.removeAllViews();
    }
}
