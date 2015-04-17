package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.menu_desplegable.CasosUso;
import com.arkamovil.android.procesos.CrearTablas;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Login;

import java.util.ArrayList;
import java.util.List;

public class CasoUso5 extends Fragment {

    private int contador1=0;
    private int contador2=0;
    private AutoCompleteTextView dep;
    private AutoCompleteTextView fun;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fm_casouso5, container, false);

        dep = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia);
        fun = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario);

        //Se envia parametros de vista y de campo AutoComplete al web service de dependencias.
        if(contador1==0) {
            WS_Dependencia cargar_dependencias = new WS_Dependencia();
            cargar_dependencias.startWebAccess(getActivity(), dep);
            contador1++;
        }

        //Se envia parametros de vista y de campo AutoComplete al web service de funcionarios.
        if(contador2==0) {
            WS_Funcionario cargar_funcionarios = new WS_Funcionario();
            cargar_funcionarios.startWebAccess(getActivity(), fun, dep.getListSelection() );
            contador2++;
            Log.v("Tag", dep.getListSelection()+"");
        }


        final AutoCompleteTextView funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario);
        funcionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionario.showDropDown();
            }
        });

        final AutoCompleteTextView dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia);
        dependencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dependencia.showDropDown();
            }
        });

        //Se crea un objeto de la clse Llenar Spinner y se llama la función llenar, que es la encargada de cargar los datos al Spinner.
        //Se envian como parametros la actividad y el Spinner a llenar. posiblemente tambien se envie una consulta.

        LlenarListas llenar = new LlenarListas();
        llenar.llenarSpinnerEstado(getActivity(), (Spinner) rootView.findViewById(R.id.estado));

        //////////////////////////////////////////////////////////////////////////////////////////////////

        //Clase para crear Tablas, se envian como parametros la Vista, La Actividad y el numero de Filas.
        CrearTablas crear = new CrearTablas();
        crear.crear(rootView, getActivity(), 5);
        ////////////////////////////////////////////////////////////////////////////////////////////////

        return rootView;
    }


}
