package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arkamovil.android.Informacion.Modificar_Informacion_Elementos_Scanner;
import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.GenerarPDF_Inventarios;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_Asignaciones;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Dependencia_Postgres;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
import com.arkamovil.android.servicios_web.WS_Placa;
import com.arkamovil.android.servicios_web.WS_Sede;

import java.util.ArrayList;
import java.util.List;

public class CasoUso4 extends Fragment {

    private AutoCompleteTextView sede;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView funcionario;
    private Button scanear;

    private static String string_sede;
    private static String string_funcionario;

    private Thread thread_actualizarregistro;

    private Handler handler = new Handler();

    private List<String> lista_sede = new ArrayList<String>();
    private List<String> lista_dependencia = new ArrayList<String>();
    private static List<String> lista_funcionario = new ArrayList<String>();


    public static List<String> getLista_funcionario() {
        return lista_funcionario;
    }

    private ImageView bajar;
    private ImageView subir;

    private WS_Elemento elem;

    private View rootView;


    private int seleccion = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_casouso4, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c4);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_c4);
        funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_c4);
        bajar = (ImageView) rootView.findViewById(R.id.bajar_c4);
        subir = (ImageView) rootView.findViewById(R.id.subir_c4);

        bajar.setVisibility(View.INVISIBLE);
        subir.setVisibility(View.INVISIBLE);

        //Se envia parametros de vista y de dependencia seleccionada en el campo AutoComplete al web service de dependencias.

        WS_Sede ws_sede = new WS_Sede();
        ws_sede.startWebAccess(getActivity(), sede);
        lista_sede = ws_sede.getSede();

        new Despliegue(sede);

        sede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lista_sede.size(); i++) {
                    if (String.valueOf(sede.getText()).equals(lista_sede.get(i))) {
                        seleccion = i;
                    }
                }
                //Se envia parametros de vista y de campo AutoComplete al web service de facultad.
                WS_Dependencia ws_dependencia = new WS_Dependencia();
                ws_dependencia.startWebAccess(getActivity(), dependencia, lista_sede.get(seleccion));

//                WS_Dependencia_Postgres ws_dependencia = new WS_Dependencia_Postgres();
//                ws_dependencia.startWebAccess(getActivity(), dependencia);

                lista_dependencia = ws_dependencia.getDependencia();

                string_sede = String.valueOf(sede.getText());

                dependencia.setText("");
                dependencia.requestFocus();

                limpiarTabla();

                //Se despliegan los datos obtenidos de la dependencia.
                new Despliegue(dependencia);
            }
        });

        //Se genera esta función cuando se selecciona un item de la lista
        dependencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lista_dependencia.size(); i++) {
                    if (String.valueOf(dependencia.getText()).equals(lista_dependencia.get(i))) {
                        seleccion = i;
                    }
                }
                //Se envia parametros de vista y de campo AutoComplete al web service de funcionarios.

//                WS_Funcionario ws_funcionario = new WS_Funcionario();
//                ws_funcionario.startWebAccess(getActivity(), funcionario, lista_dependencia.get(seleccion));

                WS_Funcionario_Oracle ws_funcionario = new WS_Funcionario_Oracle();
                ws_funcionario.startWebAccess(getActivity(), funcionario, lista_dependencia.get(seleccion));

                lista_funcionario = ws_funcionario.getFuncionario();

                funcionario.setText("");
                funcionario.requestFocus();

                limpiarTabla();

                new Despliegue(funcionario);
            }
        });

        funcionario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                limpiarTabla();

                elem = new WS_Elemento();
                elem.startWebAccess(rootView, getActivity(), String.valueOf(funcionario.getText()), 3);
                string_funcionario = String.valueOf(funcionario.getText());
            }
        });

        //boton para bajar los elementos
        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaModificarInventario baj = new TablaModificarInventario();
                baj.bajar(rootView, getActivity());
            }
        });
        //llamar metodo en clase CrearTablas para ir hacia los primeros registros mostrados en la tabla
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaModificarInventario sub = new TablaModificarInventario();
                sub.subir(rootView, getActivity());
            }
        });

        //<!--IMPORTANTE--!>
        //El proceso que se encarga de leer el código de barras  se encuentra en la clase casos de uso ya que es la actividad principal (Super) y solo desde allí se pueden generar los procesos.


        scanear = (Button) rootView.findViewById(R.id.escanear_c4);
        scanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.arkamovil.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });

        Button guardar;
        guardar = (Button) rootView.findViewById(R.id.generarpdf_c4);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thread_actualizarregistro = new Thread() {
                    public void run() {

                        //CasoUso4 casoUso4 = new CasoUso4();


                        GenerarPDF_Inventarios generar = new GenerarPDF_Inventarios();
                        generar.generar(getResources(),"uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho");
//                            String id_elementoGuardar = datos.getId_elemento().get(i);
//                            String elementoGuardar = String.valueOf(elemento.getText());
//                            String placaGuardar = String.valueOf(placa.getText());
//                            String serieGuardar = String.valueOf(serie.getText());
//                            String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
//                            String observacionGuardar = String.valueOf(observacion.getText());
//                            WS_ActualizarInventario ws_actualizarInventario = new WS_ActualizarInventario();
//                            ws_actualizarInventario.startWebAccess(c, id_elementoGuardar, serieGuardar, placaGuardar,estadoGuardar,observacionGuardar);

                        handler.post(createUI);
                    }
                };

                thread_actualizarregistro.start();
            }
        });


        return rootView;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //En esta función se llama a la libreria encargada de leer y obtener la información de los códigos de barras despues de que se ha generado el intent.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == getActivity().RESULT_OK) {
            String contenido = intent.getStringExtra("SCAN_RESULT");
            String formato = intent.getStringExtra("SCAN_RESULT_FORMAT");

            //scanear.setText(contenido);

            WS_Placa ws_placa = new WS_Placa();
            ws_placa.startWebAccess(rootView, getActivity(), contenido);

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            // Si se cancelo la captura.
        }

    }

    public void limpiarTabla(){

        TablaModificarInventario borrar = new TablaModificarInventario();
        //borrar.borrarTabla(rootView, getActivity());
        bajar.setVisibility(View.INVISIBLE);
        subir.setVisibility(View.INVISIBLE);
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            Toast.makeText(getActivity(), "Se ha generado el pdf en la carpeta -> Download -> Inventarios", Toast.LENGTH_LONG).show();
        }
    };

    public static String getString_sede() {
        return string_sede;
    }

    public static String getString_funcionario() {
        return string_funcionario;
    }
}

