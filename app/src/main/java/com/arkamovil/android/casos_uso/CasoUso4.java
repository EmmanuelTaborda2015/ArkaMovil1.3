package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.menu_desplegable.CasosUso;


public class CasoUso4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_casouso4, container, false);
        return rootView;
    }

}