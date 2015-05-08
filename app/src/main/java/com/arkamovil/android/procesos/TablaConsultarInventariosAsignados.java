package com.arkamovil.android.procesos;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arkamovil.android.Informacion.Asignaciones;
import com.arkamovil.android.Informacion.Informacion_Elementos;
import com.arkamovil.android.R;
import com.arkamovil.android.servicios_web.WS_Asignaciones;

import java.util.ArrayList;
import java.util.List;

public class TablaConsultarInventariosAsignados {

    TableLayout tabla;
    TableLayout cabecera;

    private int tamanoPantalla;

    TableRow.LayoutParams layoutFila;
    TableRow.LayoutParams layoutId;
    TableRow.LayoutParams layoutTexto;
    TableRow.LayoutParams layoutVer;

    private static Activity act;

    Resources rs;
    private static List<String> id_elemento;
    private static List<String> descripcion;

    private static int inicio = -5;
    private static int contador = 0;


    private int MAX_FILAS = 0;


    public void crear(View rootView, Activity actividad, List<String> id, List<String> desc) {

        this.act = actividad;
        this.MAX_FILAS = 5;

        this.id_elemento = id;
        this.descripcion = desc;

        tamanoPantalla = rootView.getWidth();

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c4);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c4);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla*0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla*0.40), TableRow.LayoutParams.MATCH_PARENT);
        layoutVer = new TableRow.LayoutParams((int) (tamanoPantalla*0.30), TableRow.LayoutParams.MATCH_PARENT);

        int val1 = 0;
        int val2 = 0;

        if (id_elemento.size() > 0) {
            val1 = ((int) id_elemento.size() / 5);
            val2 = id_elemento.size() % 5;
        }

        if (contador < val1) {
            this.inicio = this.inicio + 5;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
        } else if (contador == val1) {
            this.inicio = this.inicio + 5;
            MAX_FILAS = val2;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
        }

        contador++;
    }

    public void agregarCabecera() {
        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;
        TextView txtInfo;


        fila = new TableRow(act);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(act);
        txtDescripcion = new TextView(act);
        txtInfo = new TextView(act);

        txtId.setText("Id");
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setTextAppearance(act, R.style.etiqueta);
        txtId.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtId.setLayoutParams(layoutId);

        txtDescripcion.setText("Descripción");
        txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDescripcion.setTextAppearance(act, R.style.etiqueta);
        txtDescripcion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDescripcion.setLayoutParams(layoutTexto);

        txtInfo.setText("Ver");
        txtInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInfo.setTextAppearance(act, R.style.etiqueta);
        txtInfo.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtInfo.setLayoutParams(layoutVer);

        fila.addView(txtId);
        fila.addView(txtDescripcion);
        fila.addView(txtInfo);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla() {

        TableRow fila;
        TextView txtId;
        TextView txtDescripcion;
        ImageView txtVer;

        for (int i = 0; i < MAX_FILAS; i++) {
            fila = new TableRow(act);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(act);
            txtDescripcion = new TextView(act);
            txtVer = new ImageView(act);

            txtId.setText(id_elemento.get(this.inicio + i));
            txtId.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            txtId.setTextAppearance(act, R.style.etiqueta);
            txtId.setBackgroundResource(R.drawable.tabla_celda);
            txtId.setLayoutParams(layoutId);

            txtDescripcion.setText(descripcion.get(this.inicio + i));
            txtId.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            txtDescripcion.setTextAppearance(act, R.style.etiqueta);
            txtDescripcion.setBackgroundResource(R.drawable.tabla_celda);
            txtDescripcion.setLayoutParams(layoutTexto);

            txtVer.setImageResource(R.drawable.ver);
            txtVer.setId(this.inicio + i);
            txtVer.setBackgroundResource(R.drawable.tabla_celda);
            txtVer.setLayoutParams(layoutVer);
            txtVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WS_Asignaciones ws_asignaciones = new WS_Asignaciones();
                    ws_asignaciones.startWebAccess(act, id_elemento.get(v.getId()));
                }
            });

            fila.addView(txtId);
            fila.addView(txtDescripcion);
            fila.addView(txtVer);

            tabla.addView(fila);

        }
    }

    public void bajar(View rootView, Activity actividad) {

        this.act = actividad;
        this.MAX_FILAS = 5;

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c4);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c4);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla*0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla*0.40), TableRow.LayoutParams.MATCH_PARENT);
        layoutVer = new TableRow.LayoutParams((int) (tamanoPantalla*0.30), TableRow.LayoutParams.MATCH_PARENT);

        int val1 = 0;
        int val2 = 0;

        if (this.id_elemento.size() > 0) {
            val1 = ((int) this.id_elemento.size() / 5);
            val2 = this.id_elemento.size() % 5;
        }

        if (contador < val1) {
            this.inicio = this.inicio + 5;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
            contador++;
        } else if (contador == val1) {
            this.inicio = this.inicio + 5;
            MAX_FILAS = val2;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
            contador++;
        }
    }

    public void subir(View rootView, Activity actividad) {

        this.act = actividad;
        this.MAX_FILAS = 5;

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c4);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c4);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla*0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla*0.40), TableRow.LayoutParams.MATCH_PARENT);
        layoutVer = new TableRow.LayoutParams((int) (tamanoPantalla*0.30), TableRow.LayoutParams.MATCH_PARENT);

        int val1 = 0;
        int val2 = 0;

        if (this.id_elemento.size() > 0) {
            val1 = ((int) this.id_elemento.size() / 5);
            val2 = this.id_elemento.size() % 5;
        }

        if (contador > 1) {
            this.inicio = this.inicio - 5;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
            contador--;
        } else if (contador == val1) {
            this.inicio = this.inicio - 5;
            MAX_FILAS = val2;
            tabla.removeAllViews();
            cabecera.removeAllViews();
            agregarCabecera();
            agregarFilasTabla();
            contador--;
        }
    }

    public void borrarTabla(View rootView, Activity actividad) {

        this.act = actividad;
        this.MAX_FILAS = 5;

        rs = actividad.getResources();
        tabla = (TableLayout) rootView.findViewById(R.id.tabla_c4);
        cabecera = (TableLayout) rootView.findViewById(R.id.cabecera_c4);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla*0.2), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla*0.40), TableRow.LayoutParams.MATCH_PARENT);
        layoutVer = new TableRow.LayoutParams((int) (tamanoPantalla*0.30), TableRow.LayoutParams.MATCH_PARENT);


        inicio = -5;
        contador = 0;

        id_elemento = new ArrayList<String>();
        descripcion = new ArrayList<String>();

        tabla.removeAllViews();
        cabecera.removeAllViews();
    }
}
