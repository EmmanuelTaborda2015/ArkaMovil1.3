package com.arkamovil.android.casos_uso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaConsultarInventario;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Funcionario;

import java.util.ArrayList;
import java.util.List;

public class CasoUso5 extends Fragment {

    private AutoCompleteTextView dep;
    private AutoCompleteTextView fun;
    private ImageView bajar;
    private ImageView subir;

    private List<String> lista_dependencia = new ArrayList<String>();
    private int dependencia_seleccionada = -1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fm_casouso5, container, false);

        dep = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia);
        fun = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario);
        bajar = (ImageView) rootView.findViewById(R.id.bajar);
        subir = (ImageView) rootView.findViewById(R.id.subir);

        bajar.setVisibility(View.INVISIBLE);
        subir.setVisibility(View.INVISIBLE);

        //Se envia parametros de vista y de campo AutoComplete al web service de dependencias.

        WS_Dependencia cargar_dependencias = new WS_Dependencia();
        cargar_dependencias.startWebAccess(getActivity(), dep);
        lista_dependencia = cargar_dependencias.getDependecia();


        Despliegue despFuncionario = new Despliegue(fun);
        Despliegue despDependencia = new Despliegue(dep);

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
                TablaConsultarInventario borrar = new TablaConsultarInventario();
                borrar.borrarTabla(rootView, getActivity());
                bajar.setVisibility(View.INVISIBLE);
                subir.setVisibility(View.INVISIBLE);


            }
        });

        fun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!"".equals(String.valueOf(dep.getText()))) {
                    WS_Elemento elem = new WS_Elemento();
                    elem.startWebAccess(rootView, getActivity(), String.valueOf(fun.getText()), 1);
                }
            }
        });

        //boton para bajar los elementos
        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaConsultarInventario baj = new TablaConsultarInventario();
                baj.bajar(rootView, getActivity());
            }
        });
        //llamar metodo en clase CrearTablas para ir hacia los primeros registros mostrados en la tabla
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaConsultarInventario sub = new TablaConsultarInventario();
                sub.subir(rootView, getActivity());
            }
        });

        return rootView;
    }


}
