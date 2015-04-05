package com.arkamovil.android.procesos;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class LlenarSpinner extends Fragment{

    public ArrayAdapter<String> llenar(Activity act,Spinner spin) {
        List<String> toSpin = new ArrayList<String>();
        toSpin.add("sede 45");
        toSpin.add("Sede Vivero");
        toSpin.add("Sede Macarena");
        toSpin.add("Sede Tecnologica");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_spinner_item,toSpin);
        spin.setAdapter(adapter);
        return  adapter;
    }
}
