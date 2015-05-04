package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_ActualizarInventario;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_RegistroActaVisita;

public class Modificar_Informacion_Elementos extends Dialog {


    private Activity c;
    private int i;
    private TextView elemento;
    private TextView placa;
    private TextView serie;
    private TextView observacion;
    private Spinner estadoSpin;

    private WS_Elemento datos;

    private Thread thread_actualizarregistro;

    private Handler handler = new Handler();

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
        observacion = (TextView) findViewById(R.id.observacion_61);

        estadoSpin = (Spinner) findViewById(R.id.estado_61);

       datos = new WS_Elemento();

        elemento.setText(datos.getDescripcion().get(i));
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
                String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());

                if ("--Seleccione una opción--".equals(estadoGuardar)) {
                    Toast.makeText(c, "Porfavor seleccione el estado", Toast.LENGTH_LONG).show();
                }else{
                    thread_actualizarregistro = new Thread() {
                        public void run() {
                            String id_elementoGuardar = datos.getId_elemento().get(i);
                            String elementoGuardar = String.valueOf(elemento.getText());
                            String placaGuardar = String.valueOf(placa.getText());
                            String serieGuardar = String.valueOf(serie.getText());
                            String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
                            String observacionGuardar = String.valueOf(observacion.getText());
                            WS_ActualizarInventario ws_actualizarInventario = new WS_ActualizarInventario();
                            ws_actualizarInventario.startWebAccess(c, id_elementoGuardar, serieGuardar, placaGuardar,estadoGuardar,observacionGuardar);

                            handler.post(createUI);
                        }
                    };

                    thread_actualizarregistro.start();
                }
            }
        });

        LlenarListas estado = new LlenarListas();
        estado.llenarSpinnerEstado1(c, estadoSpin);
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            Toast.makeText(c, "Se ha Actualizado el inventario Asignado", Toast.LENGTH_LONG).show();
            TablaModificarInventario cerrarDialog = new TablaModificarInventario();
            cerrarDialog.cerrarDialog();

        }
    };

}