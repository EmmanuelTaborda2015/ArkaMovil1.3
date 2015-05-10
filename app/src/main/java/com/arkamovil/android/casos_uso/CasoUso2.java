package com.arkamovil.android.casos_uso;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaAsignarInventarios;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_ElementosAsignar;
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
import com.arkamovil.android.servicios_web.WS_Sede;

import java.util.ArrayList;
import java.util.List;

public class CasoUso2 extends Fragment {

    private AutoCompleteTextView sede;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView funcionario;

    private static String string_sede;
    private static String string_funcionario;

    private Thread thread_actualizarregistro;

    private Handler handler = new Handler();

    private List<String> lista_sede = new ArrayList<String>();
    private List<String> lista_dependencia = new ArrayList<String>();
    private static List<String> lista_funcionario = new ArrayList<String>();

    private ImageView bajar;
    private ImageView subir;

    private WS_ElementosAsignar elem;

    private static View rootView;


    private int seleccion = 0;


    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_asignar_inventarios, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c2);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_c2);
        funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_c2);
        bajar = (ImageView) rootView.findViewById(R.id.bajar_c2);
        subir = (ImageView) rootView.findViewById(R.id.subir_c2);

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

                elem = new WS_ElementosAsignar();
                elem.startWebAccess(rootView, getActivity());
                string_funcionario = String.valueOf(funcionario.getText());
            }
        });

        //boton para bajar los elementos
        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaAsignarInventarios baj = new TablaAsignarInventarios();
                baj.bajar(rootView, getActivity());
            }
        });
        //llamar metodo en clase CrearTablas para ir hacia los primeros registros mostrados en la tabla
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaAsignarInventarios sub = new TablaAsignarInventarios();
                sub.subir(rootView, getActivity());
            }
        });


        return rootView;
    }

    public void limpiarTabla() {

        TablaAsignarInventarios borrar = new TablaAsignarInventarios();
        borrar.borrarTabla(rootView, getActivity());
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

    public static List<String> getLista_funcionario() {
        return lista_funcionario;
    }
}

