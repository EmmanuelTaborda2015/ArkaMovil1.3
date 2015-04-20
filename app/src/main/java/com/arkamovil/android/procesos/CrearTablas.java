package com.arkamovil.android.procesos;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arkamovil.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/04/15.
 */
public class CrearTablas {

    TableLayout tabla;
    TableLayout cabecera;
    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutId;
    TableRow.LayoutParams layoutTexto;
    TableRow.LayoutParams layoutVer;

    Activity act;

    Resources rs;
    private  List<String> id_elemento;
    private  List<String> descripcion;

    TextView txtVer[] = new TextView[5];


    private int MAX_FILAS;

    public void crear(View rootView, Activity actividad, List<String> id, List<String> desc){

        this.act = actividad;
        this.MAX_FILAS = 5;

        this.id_elemento = id;
        this.descripcion = desc;

        Log.v("Aqui",id_elemento.get(0));

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
        layoutTexto = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
        layoutVer = new TableRow.LayoutParams(100,TableRow.LayoutParams.WRAP_CONTENT);
        agregarCabecera();
        agregarFilasTabla();
    }

    public void agregarCabecera(){
        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;
        TextView txtInfo;


        fila = new TableRow(act);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(act);
        txtDescripcion = new TextView(act);
        txtInfo = new TextView(act);

        txtId.setText("Id_elemento");
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(act,R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtId.setLayoutParams(layoutId);

        txtDescripcion.setText("Descripción");
        txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDescripcion.setTextAppearance(act,R.style.etiqueta);
        txtDescripcion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDescripcion.setLayoutParams(layoutTexto);

        txtInfo.setText("Ver");
        txtInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInfo.setTextAppearance(act,R.style.etiqueta);
        txtInfo.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtInfo.setLayoutParams(layoutVer);

        fila.addView(txtId);
        fila.addView(txtDescripcion);
        fila.addView(txtInfo);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla(){

        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;


        for(int i = 0;i<MAX_FILAS;i++){
            fila = new TableRow(act);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(act);
            txtDescripcion = new TextView(act);
            txtVer[i] = new TextView(act);

            txtId.setText(id_elemento.get(i));
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setTextAppearance(act,R.style.etiqueta);
            txtId.setBackgroundResource(R.drawable.tabla_celda);
            txtId.setLayoutParams(layoutId);

            txtDescripcion.setText(descripcion.get(i));
            txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
            txtDescripcion.setTextAppearance(act,R.style.etiqueta);
            txtDescripcion.setBackgroundResource(R.drawable.tabla_celda);
            txtDescripcion.setLayoutParams(layoutTexto);

            txtVer[i].setText("ver");
            txtVer[i].setId(i);
            txtVer[i].setGravity(Gravity.CENTER_HORIZONTAL);
            txtVer[i].setTextAppearance(act,R.style.etiqueta);
            txtVer[i].setBackgroundResource(R.drawable.tabla_celda);
            txtVer[i].setLayoutParams(layoutVer);
            txtVer[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("Aqui", v.getId()+"");
                }
            });

            fila.addView(txtId);
            fila.addView(txtDescripcion);
            fila.addView(txtVer[i]);

            tabla.addView(fila);
        }
    }
}
