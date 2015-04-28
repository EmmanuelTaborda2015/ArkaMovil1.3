package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.CasoUso6;
import com.arkamovil.android.procesos.LlenarListas;

public class Modificar_Informacion_Elementos_Scanner extends Dialog {


    private Activity c;
    private int i;
    private TextView elemento;
    private TextView placa;
    private TextView serie;
    private Spinner estadoSpin;

    public Modificar_Informacion_Elementos_Scanner(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_modificar_scanner);

        elemento = (TextView) findViewById(R.id.elemento_61s);
        placa = (TextView) findViewById(R.id.placa_61s);
        serie = (TextView) findViewById(R.id.serial_61s);

        estadoSpin = (Spinner) findViewById(R.id.estado_61s);

//        WS_Elemento datos = new WS_Elemento();
//
//        elemento.setText(datos.getDescripcion().get(i));
//        serie.setText(datos.getMarca().get(i));
//        placa.setText(datos.getPlaca().get(i));
//        serie.setText(datos.getSerie().get(i));

        Button cancelar;
        cancelar = (Button) findViewById(R.id.cancelar_61s);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CasoUso6 cerrar = new CasoUso6();
                cerrar.cerrarDialog();
            }
        });

        Button guardar;
        guardar = (Button) findViewById(R.id.guardar_61s);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String elementoGuardar = String.valueOf(elemento.getText());
                String placaGuardar = String.valueOf(placa.getText());
                String serieGuardar = String.valueOf(serie.getText());
                String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
            }
        });

        LlenarListas estado = new LlenarListas();
        estado.llenarSpinnerEstado2(c, estadoSpin);
    }

}