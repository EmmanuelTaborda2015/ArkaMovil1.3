package com.arkamovil.android.casos_uso;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.servicios_web.WS_InventarioTipoConfirmacion;

import java.util.List;

public class ElementosInventario extends Fragment {

    private Thread thread;
    private Handler handler = new Handler();

    private WS_InventarioTipoConfirmacion inventario;

    private View rootView;
    private int offset = 0;
    private int limit = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_inventario, container, false);

//        final String estado  = getArguments().getString("estado");
//        final String criterio  = getArguments().getString("criterio");
//        final String dato  = getArguments().getString("dato");
//
//        Toast.makeText(getActivity(), estado + " " + criterio + " " + dato , Toast.LENGTH_LONG).show();

        thread = new Thread() {
            public void run() {
                inventario = new WS_InventarioTipoConfirmacion();
                inventario.startWebAccess("A", "B", "C", offset, limit);

                handler.post(createUI);
            }
        };

        thread.start();

        ImageView bajar = (ImageView) rootView.findViewById(R.id.subir_elementos_inventario);
        ImageView subir = (ImageView) rootView.findViewById(R.id.bajar_elementos_inventario);

        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajar();
            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subir();
            }
        });

        return rootView;
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            crear(rootView, getActivity(), inventario.getDoc_fun(), inventario.getSede(), inventario.getDependencia(), inventario.getDependencia());
        }
    };

    public void limpiarTabla() {
        borrarTabla();
    }


    //************DESDE AQUÍ SE CREAN LOS EVENTOS DE LA TABLA************//

    private double f1 = 0.20;
    private double f2 = 0.35;
    private double f3 = 0.14;
    private double f4 = 0.14;
    private double f5 = 0.14;

    private static TableLayout tabla;
    private static TableLayout cabecera;

    private static TableRow.LayoutParams layoutFila;
    private static TableRow.LayoutParams layoutPlaca;
    private static TableRow.LayoutParams layoutDescripcion;
    private static TableRow.LayoutParams layoutEstadoAprob;
    private static TableRow.LayoutParams layoutObservacion;
    private static TableRow.LayoutParams layoutDetalle;

    private static Activity actividad;
    private static View vista;

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
            Toast.makeText(actividad, "No existen inventarios para los criterios seleccionados.", Toast.LENGTH_LONG).show();
        }


    }

    public void agregarCabecera() {

        TableRow fila;
        TextView txtPlaca;
        TextView txtDescripcion;
        TextView txtEstadoAprob;
        TextView txtObservacion;
        TextView txtDetalle;

        fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        txtPlaca = new TextView(actividad);
        txtDescripcion = new TextView(actividad);
        txtEstadoAprob = new TextView(actividad);
        txtObservacion = new TextView(actividad);
        txtDetalle = new TextView(actividad);

        txtPlaca.setText("Documento");
        txtPlaca.setGravity(Gravity.CENTER_HORIZONTAL);
        txtPlaca.setTextAppearance(actividad, R.style.etiqueta);
        txtPlaca.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtPlaca.setLayoutParams(layoutPlaca);

        txtDescripcion.setText("Nombre");
        txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDescripcion.setTextAppearance(actividad, R.style.etiqueta);
        txtDescripcion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDescripcion.setLayoutParams(layoutDescripcion);

        txtEstadoAprob.setText("Estado");
        txtEstadoAprob.setGravity(Gravity.CENTER_HORIZONTAL);
        txtEstadoAprob.setTextAppearance(actividad, R.style.etiqueta);
        txtEstadoAprob.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtEstadoAprob.setLayoutParams(layoutEstadoAprob);

        txtObservacion.setText("Info");
        txtObservacion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtObservacion.setTextAppearance(actividad, R.style.etiqueta);
        txtObservacion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtObservacion.setLayoutParams(layoutObservacion);

        txtDetalle.setText("Obser");
        txtDetalle.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDetalle.setTextAppearance(actividad, R.style.etiqueta);
        txtDetalle.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDetalle.setLayoutParams(layoutDetalle);

        fila.addView(txtPlaca);
        fila.addView(txtDescripcion);
        fila.addView(txtEstadoAprob);
        fila.addView(txtObservacion);
        fila.addView(txtDetalle);

        cabecera.addView(fila);
    }

    public void agregarFilasTabla() {

        TableRow fila;
        TextView txtPlaca;
        TextView txtDescripcion;
        CheckBox txtEstadoAprob;
        ImageView txtObservacion;
        ImageView txtDetalle;

        for (int i = 0; i < MAX_FILAS; i++) {
            fila = new TableRow(actividad);
            fila.setLayoutParams(layoutFila);

            txtPlaca = new TextView(actividad);
            txtDescripcion = new TextView(actividad);
            txtEstadoAprob = new CheckBox(actividad);
            txtObservacion = new ImageView(actividad);
            txtDetalle = new ImageView(actividad);

            txtPlaca.setText(doc_fun.get(inicio + i));
            txtPlaca.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtPlaca.setTextAppearance(actividad, R.style.etiqueta);
            txtPlaca.setBackgroundResource(R.drawable.tabla_celda);
            txtPlaca.setLayoutParams(layoutPlaca);

            txtDescripcion.setText(doc_fun.get(inicio + i));
            txtDescripcion.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtDescripcion.setTextAppearance(actividad, R.style.etiqueta);
            txtDescripcion.setBackgroundResource(R.drawable.tabla_celda);
            txtDescripcion.setLayoutParams(layoutDescripcion);

            txtEstadoAprob.setId(this.inicio + i);
            txtEstadoAprob.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtEstadoAprob.setTextAppearance(actividad, R.style.etiqueta);
            txtEstadoAprob.setBackgroundResource(R.drawable.tabla_celda);
            txtEstadoAprob.setLayoutParams(layoutEstadoAprob);

            txtObservacion.setImageResource(R.drawable.ver);
            txtObservacion.setId(this.inicio + i);
            txtObservacion.setBackgroundResource(R.drawable.tabla_celda);
            txtObservacion.setLayoutParams(layoutObservacion);
            txtObservacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog = new Informacion_Elementos_Cedula(actividad, v.getId());
                    //dialog.show();
                }
            });

            txtDetalle.setImageResource(R.drawable.modificar);
            txtDetalle.setId(this.inicio + i);
            txtDetalle.setBackgroundResource(R.drawable.tabla_celda);
            txtDetalle.setLayoutParams(layoutDetalle);
            txtDetalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog = new Informacion_Elementos_Cedula(actividad, v.getId());
                    //dialog.show();
                }
            });


            fila.addView(txtPlaca);
            fila.addView(txtDescripcion);
            fila.addView(txtEstadoAprob);
            fila.addView(txtObservacion);
            fila.addView(txtDetalle);

            tabla.addView(fila);
        }
    }

    public void cargarElementos() {

        tabla = (TableLayout) vista.findViewById(R.id.tabla_elementos_inventario);
        cabecera = (TableLayout) vista.findViewById(R.id.cabecera_elementos_inventario);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutPlaca = new TableRow.LayoutParams((int) (tamanoPantalla * f1), TableRow.LayoutParams.MATCH_PARENT);
        layoutDescripcion = new TableRow.LayoutParams((int) (tamanoPantalla * f2), TableRow.LayoutParams.MATCH_PARENT);
        layoutEstadoAprob = new TableRow.LayoutParams((int) (tamanoPantalla * f3), TableRow.LayoutParams.MATCH_PARENT);
        layoutObservacion = new TableRow.LayoutParams((int) (tamanoPantalla * f4), TableRow.LayoutParams.MATCH_PARENT);
        layoutDetalle = new TableRow.LayoutParams((int) (tamanoPantalla * f5), TableRow.LayoutParams.MATCH_PARENT);

        tabla.removeAllViews();

    }

    public void bajar() {

        if (this.inicio <= (doc_fun.size() - (factor + 1))) {
            cargarElementos();
            this.inicio++;
            agregarFilasTabla();
        }
    }

    public void subir() {

        if (this.inicio > 0) {
            cargarElementos();
            this.inicio--;
            agregarFilasTabla();
        }
    }

    public void borrarTabla() {

        tabla = (TableLayout) vista.findViewById(R.id.tabla_elementos_inventario);
        cabecera = (TableLayout) vista.findViewById(R.id.cabecera_elementos_inventario);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutPlaca = new TableRow.LayoutParams((int) (tamanoPantalla * f1), TableRow.LayoutParams.MATCH_PARENT);
        layoutDescripcion = new TableRow.LayoutParams((int) (tamanoPantalla * f2), TableRow.LayoutParams.MATCH_PARENT);
        layoutEstadoAprob = new TableRow.LayoutParams((int) (tamanoPantalla * f3), TableRow.LayoutParams.MATCH_PARENT);
        layoutObservacion = new TableRow.LayoutParams((int) (tamanoPantalla * f4), TableRow.LayoutParams.MATCH_PARENT);
        layoutDetalle = new TableRow.LayoutParams((int) (tamanoPantalla * f5), TableRow.LayoutParams.MATCH_PARENT);

        tabla.removeAllViews();
        cabecera.removeAllViews();
    }
}

