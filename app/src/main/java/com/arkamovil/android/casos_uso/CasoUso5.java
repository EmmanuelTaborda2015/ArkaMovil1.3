package com.arkamovil.android.casos_uso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.LlenarListas;

import java.util.ArrayList;
import java.util.List;

public class CasoUso5 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_casouso5, container, false);

        final LlenarListas llenarA = new LlenarListas();
        llenarA.llenarAutoComplete(getActivity(), (AutoCompleteTextView) rootView.findViewById(R.id.funcionario));

        final LlenarListas llenarB = new LlenarListas();
        llenarB.llenarAutoComplete(getActivity(), (AutoCompleteTextView) rootView.findViewById(R.id.dependencia));


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
        llenar.llenarSpinnerEstado(getActivity(),(Spinner) rootView.findViewById(R.id.estado));

        //////////////////////////////////////////////////////////////////////////////////////////////////


        return rootView;
    }
}
