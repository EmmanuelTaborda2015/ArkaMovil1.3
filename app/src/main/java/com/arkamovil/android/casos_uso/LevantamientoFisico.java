package com.arkamovil.android.casos_uso;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.TablaConsultarInventariosAsignados;
import com.arkamovil.android.servicios_web.WS_InventarioTipoConfirmacion;


public class LevantamientoFisico extends Fragment {

    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_levantamiento_fisico, container, false);

        WS_InventarioTipoConfirmacion elem2 = new WS_InventarioTipoConfirmacion();
        elem2.startWebAccess(rootView, getActivity());

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        WS_InventarioTipoConfirmacion elem2 = new WS_InventarioTipoConfirmacion();
        elem2.startWebAccess(rootView, getActivity());
    }

//    public void limpiarTabla() {
//
//        TablaConsultarInventariosAsignados borrar = new TablaConsultarInventariosAsignados();
//        borrar.borrarTabla(rootView, getActivity());
//        bajar.setVisibility(View.GONE);
//        subir.setVisibility(View.GONE);
//
//    }
}

