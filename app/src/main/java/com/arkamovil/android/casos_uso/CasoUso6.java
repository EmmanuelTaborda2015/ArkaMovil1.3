package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.arkamovil.android.Informacion.Modificar_Informacion_Elementos_Scanner;
import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Dependencia_Postgres;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Placa;
import com.arkamovil.android.servicios_web.WS_Sede;

import java.util.ArrayList;
import java.util.List;

public class CasoUso6 extends Fragment {

    private AutoCompleteTextView sede;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView funcionario;
    private Button scanear;

    private List<String> lista_sede = new ArrayList<String>();
    private List<String> lista_dependencia = new ArrayList<String>();
    private List<String> lista_funcionario = new ArrayList<String>();



    private ImageView bajar;
    private ImageView subir;

    private WS_Elemento elem;

    private View rootView;

    private int seleccion = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_casouso6, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c6);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_6);
        funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_6);
        bajar = (ImageView) rootView.findViewById(R.id.bajar_6);
        subir = (ImageView) rootView.findViewById(R.id.subir_6);

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
                //WS_Dependencia ws_dependencia = new WS_Dependencia();
                //ws_dependencia.startWebAccess(getActivity(), dependencia, lista_sede.get(seleccion), "null");

                WS_Dependencia_Postgres ws_dependencia = new WS_Dependencia_Postgres();
                ws_dependencia.startWebAccess(getActivity(), dependencia);

                lista_dependencia = ws_dependencia.getDependencia();

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

                WS_Funcionario ws_funcionario = new WS_Funcionario();
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
                    elem.startWebAccess(rootView, getActivity(), String.valueOf(funcionario.getText()), 2);
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


        scanear = (Button) rootView.findViewById(R.id.escanear_6);
        scanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.arkamovil.android.SCAN");
                startActivityForResult(intent, 0);
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

            scanear.setText(contenido);

            WS_Placa ws_placa = new WS_Placa();
            ws_placa.startWebAccess(rootView, getActivity(), contenido);

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            // Si se cancelo la captura.
        }

    }

    public void limpiarTabla(){

        TablaModificarInventario borrar = new TablaModificarInventario();
        borrar.borrarTabla(rootView, getActivity());
        bajar.setVisibility(View.INVISIBLE);
        subir.setVisibility(View.INVISIBLE);
    }
}
