package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento_dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento_funcionario;
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
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
    private List<String> lista_documento = new ArrayList<String>();
    private static List<String> lista_documentos = new ArrayList<String>();
    private static List<String> lista_id_dependencia = new ArrayList<String>();
    private static List<String> id_elemento = new ArrayList<String>();
    private static List<String> lista_elemento_funcionario = new ArrayList<String>();

    private static int funcion = 0;

    public static List<String> getId_elemento() {
        return id_elemento;
    }

    public static int getFuncion() {
        return funcion;
    }

    private ImageView bajar;
    private ImageView subir;

    private WS_Elemento_funcionario elem;

    private View rootView;

    private int seleccion = -1;
    private int seleccion1 = -1;
    private int seleccion2 = -1;

    private static WS_Elemento_funcionario elem_fun;

    private static WS_Elemento_dependencia elem_dep;

    private static String string_sede;
    private static String string_dependencia;
    private static String string_funcionario;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_casouso6, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c6);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_c6);
        funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_c6);
        bajar = (ImageView) rootView.findViewById(R.id.bajar_c6);
        subir = (ImageView) rootView.findViewById(R.id.subir_c6);

        dependencia.setEnabled(false);

        bajar.setVisibility(View.GONE);
        subir.setVisibility(View.GONE);

        //Se envia parametros de vista y de dependencia seleccionada en el campo AutoComplete al web service de dependencias.

        WS_Sede ws_sede = new WS_Sede();
        ws_sede.startWebAccess(getActivity(), sede);
        lista_sede = ws_sede.getSede();

        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.pla_c6);
        l1.setVisibility(View.GONE);

        final LinearLayout l2 = (LinearLayout) rootView.findViewById(R.id.dep_c6);
        l2.setVisibility(View.GONE);

        final LinearLayout l3 = (LinearLayout) rootView.findViewById(R.id.fun_c6);
        l3.setVisibility(View.GONE);

        Button des1 = (Button) rootView.findViewById(R.id.des_pla_c6);
        des1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();

                if (on) {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                } else {
                    l1.setVisibility(View.GONE);
                }
            }
        });

        Button des2 = (Button) rootView.findViewById(R.id.des_dep_c6);
        des2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    l2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);


                } else {
                    l2.setVisibility(View.GONE);
                }
            }
        });

        Button des3 = (Button) rootView.findViewById(R.id.des_fun_c6);
        des3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    l3.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);

                } else {
                    l3.setVisibility(View.GONE);
                }
            }
        });

        new Despliegue(sede);

        WS_Funcionario_Oracle ws_funcionario = new WS_Funcionario_Oracle();
        ws_funcionario.startWebAccess(getActivity(), funcionario, "null");

        lista_funcionario = ws_funcionario.getFun_nombre();
        lista_documentos = ws_funcionario.getFun_identificacion();

        //new Despliegue(funcionario);


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
                lista_id_dependencia = ws_dependencia.getId_dependencia();

                dependencia.setText("");
                dependencia.requestFocus();

                limpiarTabla();

                //Se despliegan los datos obtenidos de la dependencia.
                new Despliegue(dependencia);
            }
        });

        Button con_dep = (Button) rootView.findViewById(R.id.con_dep_c6);
        con_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                limpiarTabla();

                int contador = 0;

                for (int i = 0; i < lista_sede.size(); i++) {
                    if (String.valueOf(sede.getText()).equalsIgnoreCase(lista_sede.get(i))) {
                        seleccion = i;
                    }
                }

                if (seleccion > -1) {
                    string_sede = String.valueOf(sede.getText());
                    for (int i = 0; i < lista_dependencia.size(); i++) {
                        if (String.valueOf(dependencia.getText()).equalsIgnoreCase(lista_dependencia.get(i))) {
                            seleccion1 = i;
                        }
                    }
                    if(seleccion1 > -1){
                        elem_dep = new WS_Elemento_dependencia();
                        elem_dep.startWebAccess(rootView, getActivity(), lista_id_dependencia.get(seleccion1), 2);
                        id_elemento = elem_dep.getId_elemento();
                        lista_elemento_funcionario = elem_dep.getFuncionario();
                        string_dependencia = String.valueOf(dependencia.getText());
                        funcion = 1;
                    }else{
                        Toast.makeText(getActivity(), "La Dependencia ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "La Sede ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                }

                seleccion = -1;
                seleccion1 = -1;

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        Button con_fun = (Button) rootView.findViewById(R.id.con_fun_c6);
        con_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                limpiarTabla();

                for (int i = 0; i < lista_funcionario.size(); i++) {
                    if (String.valueOf(funcionario.getText()).equalsIgnoreCase(lista_documentos.get(i))) {
                        seleccion2 = i;
                    }
                }

                if (seleccion2 > -1) {
                    elem_fun = new WS_Elemento_funcionario();
                    elem_fun.startWebAccess(rootView, getActivity(), lista_documentos.get(seleccion2), 2);
                    id_elemento = elem_fun.getId_elemento();
                    lista_elemento_funcionario = elem_fun.getFuncionario();
                    string_funcionario = String.valueOf(funcionario.getText());
                    funcion = 2;

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                } else {
                    Toast.makeText(getActivity(), "El documento ingresado no es valido, por favor verifiquelo e intente de nuevo.", Toast.LENGTH_LONG).show();
                }

                seleccion2 = -1;

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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


        scanear = (Button) rootView.findViewById(R.id.escanear_c6);
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

            //scanear.setText(contenido);

            WS_Placa ws_placa = new WS_Placa();
            ws_placa.startWebAccess(rootView, getActivity(), contenido);

            limpiarTabla();

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            // Si se cancelo la captura.
        }

    }

    public void limpiarTabla() {

        TablaModificarInventario borrar = new TablaModificarInventario();
        borrar.borrarTabla(rootView, getActivity());
        bajar.setVisibility(View.GONE);
        subir.setVisibility(View.GONE);
    }
}
