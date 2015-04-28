package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_Elemento;

public class Modificar_Informacion_Elementos extends Dialog {


    private Activity c;
    private int i;
    private TextView elemento;
    private TextView placa;
    private TextView serie;
    private Spinner estadoSpin;

    public Modificar_Informacion_Elementos(Activity a, int i) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.i = i;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_modificar);

        elemento = (TextView) findViewById(R.id.elemento_61);
        placa = (TextView) findViewById(R.id.placa_61);
        serie = (TextView) findViewById(R.id.serial_61);

        estadoSpin = (Spinner) findViewById(R.id.estado_61);

        WS_Elemento datos = new WS_Elemento();

        elemento.setText(datos.getDescripcion().get(i));
        serie.setText(datos.getMarca().get(i));
        placa.setText(datos.getPlaca().get(i));
        serie.setText(datos.getSerie().get(i));

        Button cancelar;
        cancelar = (Button) findViewById(R.id.cancelar_61);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaModificarInventario cerrarDialog = new TablaModificarInventario();
                cerrarDialog.cerrarDialog();
            }
        });

        Button guardar;
        guardar = (Button) findViewById(R.id.guardar_61);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String elementoGuardar = String.valueOf(elemento.getText());
                String placaGuardar = String.valueOf(placa.getText());
                String serieGuardar = String.valueOf(serie.getText());
                String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
                if ("--Seleccione una opción--".equals(estadoGuardar)) {
                    Toast.makeText(c, "Porfavor seleccione el estado", Toast.LENGTH_LONG).show();
                }
                Log.v("Estado", estadoGuardar);

            }
        });

        LlenarListas estado = new LlenarListas();
        estado.llenarSpinnerEstado1(c, estadoSpin);
    }

}