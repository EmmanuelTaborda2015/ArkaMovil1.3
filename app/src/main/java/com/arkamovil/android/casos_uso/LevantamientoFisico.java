package com.arkamovil.android.casos_uso;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.servicios_web.WS_InventarioTipoConfirmacion;

import java.util.List;

public class LevantamientoFisico extends Fragment {

    private Thread thread;
    private Handler handler = new Handler();

    private WS_InventarioTipoConfirmacion inventario;

    private View rootView;
    private int offset = 0;
    private int limit = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_levantamiento_fisico, container, false);

        final String estado  = getArguments().getString("estado");
        final String criterio  = getArguments().getString("criterio");
        final String dato  = getArguments().getString("dato");

        Toast.makeText(getActivity(), estado + " " + criterio + " " + dato , Toast.LENGTH_LONG).show();

        thread = new Thread() {
            public void run() {
                inventario = new WS_InventarioTipoConfirmacion();
                inventario.startWebAccess(estado, criterio, dato, offset, limit);

                handler.post(createUI);
            }
        };

        thread.start();

        ImageView bajar = (ImageView) rootView.findViewById(R.id.subir_levantamiento_fisico);
        ImageView subir = (ImageView) rootView.findViewById(R.id.bajar_levantamiento_fisico);

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
            crear(rootView, getActivity(), inventario.getId_elemento(), inventario.getNomb_fun(), inventario.getDoc_fun(), inventario.getId_sede(), inventario.getSede(), inventario.getId_dependencia(), inventario.getDependencia(), inventario.getId_espacio(), inventario.getEspacio());
        }
    };

    public void limpiarTabla() {
        borrarTabla();
    }


    //************DESDE AQUÍ SE CREAN LOS EVENTOS DE LA TABLA************//

    private double f1 = 0.18;
    private double f2 = 0.35;
    private double f3 = 0.35;
    private double f4 = 0.12;

    private static TableLayout tabla;
    private static TableLayout cabecera;

    private static TableRow.LayoutParams layoutFila;
    private static TableRow.LayoutParams layoutId;
    private static TableRow.LayoutParams layoutTexto;
    private static TableRow.LayoutParams layoutFuncionario;
    private static TableRow.LayoutParams layoutMod;

    private static Activity actividad;
    private static View vista;

    private static final int factor = 5;


    private static List<String> id_elemento;
    private static List<String> nom_fun;
    private static List<String> doc_fun;
    private static List<String> id_sede;
    private static List<String> sede;
    private static List<String> id_dependencia;
    private static List<String> dependencia;
    private static List<String> id_espacio;
    private static List<String> espacio;

    private static int inicio;

    private static int tamanoPantalla;


    private static int MAX_FILAS = 0;


    public void crear(View rootView, Activity actividad, List<String> id_elemento, List<String> nom_fun, List<String> doc_fun, List<String> id_sede, List<String> sede, List<String> id_dependencia, List<String> dependencia, List<String> id_espacio, List<String> espacio) {

        this.actividad = actividad;
        this.vista = rootView;

        this.id_elemento=id_elemento;
        this.nom_fun=nom_fun;
        this.doc_fun=doc_fun;
        this.id_sede=id_sede;
        this.sede=sede;
        this.id_dependencia=id_dependencia;
        this.dependencia=dependencia;
        this.id_espacio=id_espacio;
        this.espacio=espacio;

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
        TextView txtUbicacion;
        TextView txtDetalle;

        fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        txtPlaca = new TextView(actividad);
        txtDescripcion = new TextView(actividad);
        txtUbicacion = new TextView(actividad);
        txtDetalle = new TextView(actividad);

        txtPlaca.setText("Documento");
        txtPlaca.setGravity(Gravity.CENTER_HORIZONTAL);
        txtPlaca.setTextAppearance(actividad, R.style.etiqueta);
        txtPlaca.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtPlaca.setLayoutParams(layoutId);

        txtDescripcion.setText("Nombre");
        txtDescripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDescripcion.setTextAppearance(actividad, R.style.etiqueta);
        txtDescripcion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDescripcion.setLayoutParams(layoutTexto);

        txtUbicacion.setText("Ubicación");
        txtUbicacion.setGravity(Gravity.CENTER_HORIZONTAL);
        txtUbicacion.setTextAppearance(actividad, R.style.etiqueta);
        txtUbicacion.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtUbicacion.setLayoutParams(layoutFuncionario);

        txtDetalle.setText("Detalles");
        txtDetalle.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDetalle.setTextAppearance(actividad, R.style.etiqueta);
        txtDetalle.setBackgroundResource(R.drawable.tabla_celda_cabecera);
        txtDetalle.setLayoutParams(layoutMod);

        fila.addView(txtPlaca);
        fila.addView(txtDescripcion);
        fila.addView(txtUbicacion);
        fila.addView(txtDetalle);
        cabecera.addView(fila);
    }

    public void agregarFilasTabla() {

        TableRow fila;
        TextView txtnombre;
        TextView txtdocumento;
        TextView txtdependencia;
        ImageView txtverInventario;

        for (int i = 0; i < MAX_FILAS; i++) {
            fila = new TableRow(actividad);
            fila.setLayoutParams(layoutFila);

            txtnombre = new TextView(actividad);
            txtdocumento = new TextView(actividad);
            txtdependencia = new TextView(actividad);
            txtverInventario = new ImageView(actividad);

            txtnombre.setText(doc_fun.get(inicio + i));
            txtnombre.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtnombre.setTextAppearance(actividad, R.style.etiqueta);
            txtnombre.setBackgroundResource(R.drawable.tabla_celda);
            txtnombre.setLayoutParams(layoutId);

            txtdocumento.setText(nom_fun.get(inicio + i));
            txtdocumento.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtdocumento.setTextAppearance(actividad, R.style.etiqueta);
            txtdocumento.setBackgroundResource(R.drawable.tabla_celda);
            txtdocumento.setLayoutParams(layoutTexto);

            txtdependencia.setText(dependencia.get(inicio + i));
            txtdependencia.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            txtdependencia.setTextAppearance(actividad, R.style.etiqueta);
            txtdependencia.setBackgroundResource(R.drawable.tabla_celda);
            txtdependencia.setLayoutParams(layoutFuncionario);

            txtverInventario.setImageResource(R.drawable.ver);
            txtverInventario.setId(this.inicio + i);
            txtverInventario.setBackgroundResource(R.drawable.tabla_celda);
            txtverInventario.setLayoutParams(layoutMod);
            txtverInventario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog = new Informacion_Elementos_Cedula(actividad, v.getId());
                    //dialog.show();
                    Fragment fragment = new ElementosInventario();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });


            fila.addView(txtnombre);
            fila.addView(txtdocumento);
            fila.addView(txtdependencia);
            fila.addView(txtverInventario);

            tabla.addView(fila);
        }
    }

    public void cargarElementos() {

        tabla = (TableLayout) vista.findViewById(R.id.tabla_levantamiento_fisico);
        cabecera = (TableLayout) vista.findViewById(R.id.cabecera_levantamiento_fisico);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams((int) (tamanoPantalla * f1), TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams((int) (tamanoPantalla * f2), TableRow.LayoutParams.MATCH_PARENT);
        layoutFuncionario = new TableRow.LayoutParams((int) (tamanoPantalla * f3), TableRow.LayoutParams.MATCH_PARENT);
        layoutMod = new TableRow.LayoutParams((int) (tamanoPantalla * f4), TableRow.LayoutParams.MATCH_PARENT);

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

        tabla = (TableLayout) vista.findViewById(R.id.tabla_levantamiento_fisico);
        cabecera = (TableLayout) vista.findViewById(R.id.cabecera_levantamiento_fisico);
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

