package com.arkamovil.android.casos_uso;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Funcionario;

import java.util.ArrayList;
import java.util.List;

public class CasoUso6 extends Fragment {

    private int contador1 = 0;

    private AutoCompleteTextView dep;
    private AutoCompleteTextView fun;

    private ImageView bajar;
    private ImageView subir;

    private Despliegue despliegue;

    private WS_Elemento elem;

    View rootView;

    private List<String> lista_dependencia = new ArrayList<String>();
    private int dependencia_seleccionada = -1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_casouso6, container, false);

        dep = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_6);
        fun = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_6);
        bajar = (ImageView) rootView.findViewById(R.id.bajar_6);
        subir = (ImageView) rootView.findViewById(R.id.subir_6);

        bajar.setVisibility(View.INVISIBLE);
        subir.setVisibility(View.INVISIBLE);

        //Se envia parametros de vista y de dependencia seleccionada en el campo AutoComplete al web service de dependencias.
        if (contador1 == 0) {
            WS_Dependencia cargar_dependencias = new WS_Dependencia();
            cargar_dependencias.startWebAccess(getActivity(), dep);
            lista_dependencia = cargar_dependencias.getDependecia();
            contador1++;
        }

        despliegue = new Despliegue(dep);
        despliegue = new Despliegue(fun);


        //Se genera esta función cuando se selecciona un item de la lista
        dep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lista_dependencia.size(); i++) {
                    if (String.valueOf(dep.getText()).equals(lista_dependencia.get(i))) {
                        dependencia_seleccionada = i + 1;
                    }
                }
                //Se envia parametros de vista y de campo AutoComplete al web service de funcionarios.
                WS_Funcionario cargar_funcionarios = new WS_Funcionario();
                cargar_funcionarios.startWebAccess(getActivity(), fun, dependencia_seleccionada);
                fun.setText("");
                fun.setFocusable(true);

                TablaModificarInventario borrar = new TablaModificarInventario();
                borrar.borrarTabla(rootView, getActivity());

                bajar.setVisibility(View.INVISIBLE);
                subir.setVisibility(View.INVISIBLE);


            }
        });

        fun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!"".equals(String.valueOf(dep.getText()))) {

                    elem = new WS_Elemento();
                    elem.startWebAccess(rootView, getActivity(), String.valueOf(fun.getText()), 2);




                }
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

        return rootView;
    }
}
