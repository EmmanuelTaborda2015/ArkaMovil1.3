package com.arkamovil.android.procesos;

import android.app.Activity;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arkamovil.android.R;

/**
 * Created by Emmanuel on 7/04/15.
 */
public class CrearTablas {

    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutId;
    TableRow.LayoutParams layoutTexto;

    Activity act;

    Resources rs;

    private int MAX_FILAS;

    public void crear(View rootView, Activity actividad, int filas){

        this.act = actividad;
        this.MAX_FILAS = filas;

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(70,TableRow.LayoutParams.WRAP_CONTENT);
        layoutTexto = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
        agregarCabecera();
        agregarFilasTabla();
    }

    public void agregarCabecera(){
        TableRow fila;
        TextView txtId;
        TextView txtNombre;

        fila = new TableRow(act);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(act);
        txtNombre = new TextView(act);

        txtId.setText(rs.getString(R.string.id));
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(act,R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtId.setLayoutParams(layoutId);

        txtNombre.setText(rs.getString(R.string.valor));
        txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNombre.setTextAppearance(act,R.style.etiqueta);
        txtNombre.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtNombre.setLayoutParams(layoutTexto);

        fila.addView(txtId);
        fila.addView(txtNombre);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla(){

        TableRow fila;
        TextView txtId;
        TextView txtNombre;

        for(int i = 0;i<MAX_FILAS;i++){
            int posicion = i + 1;
            fila = new TableRow(act);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(act);
            txtNombre = new TextView(act);

            txtId.setText(String.valueOf(posicion));
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setTextAppearance(act,R.style.etiqueta);
            txtId.setBackgroundResource(R.drawable.tabla_celda);
            txtId.setLayoutParams(layoutId);

            txtNombre.setText("Texto "+posicion);
            txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
            txtNombre.setTextAppearance(act,R.style.etiqueta);
            txtNombre.setBackgroundResource(R.drawable.tabla_celda);
            txtNombre.setLayoutParams(layoutTexto);

            fila.addView(txtId);
            fila.addView(txtNombre);

            tabla.addView(fila);
        }
    }
}
