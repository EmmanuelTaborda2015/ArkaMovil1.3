package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.CasoUso6;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_ActualizarInventario;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Estado;
import com.arkamovil.android.servicios_web.WS_Placa;

public class Modificar_Informacion_Elementos_Scanner extends Dialog {


    private Activity c;
    private int i;
    private TextView elemento;
    private TextView placa;
    private TextView serie;
    private TextView observacion;
    private Spinner estadoSpin;
    private static int estado = 0;

    private WS_Elemento datos;
    WS_Placa ws_placa = new WS_Placa();

    private Thread thread_estado;

    private Thread thread_actualizarregistro;

    private Handler handler = new Handler();

    public Modificar_Informacion_Elementos_Scanner(Activity a, int estado) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.estado = estado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_modificar_scanner);

        elemento = (TextView) findViewById(R.id.elemento_61s);
        placa = (TextView) findViewById(R.id.placa_61s);
        serie = (TextView) findViewById(R.id.serial_61s);
        observacion =(TextView) findViewById(R.id.observacion_61s);

        estadoSpin = (Spinner) findViewById(R.id.estado_61s);

        ws_placa = new WS_Placa();
        placa.setText(ws_placa.getPlaca().get(0));
        elemento.setText(ws_placa.getId_elemento().get(0));
        serie.setText(ws_placa.getSerie().get(0));

        Button cancelar;
        cancelar = (Button) findViewById(R.id.cancelar_61s);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WS_Placa cerrar = new WS_Placa();
                cerrar.cerrarDialog();
            }
        });

        Button guardar;
        guardar = (Button) findViewById(R.id.guardar_61s);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());

                if ("--Seleccione una opción--".equals(estadoGuardar)) {
                    Toast.makeText(c, "Porfavor seleccione el estado", Toast.LENGTH_LONG).show();
                }else {
                    thread_actualizarregistro = new Thread() {
                        public void run() {

                            //String id_elementoGuardar = datos.getId_elemento().get(i);
                            String id_elementoGuardar = String.valueOf(elemento.getText());
                            String placaGuardar = String.valueOf(placa.getText());
                            String serieGuardar = String.valueOf(serie.getText());
                            String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
                            String observacionGuardar = String.valueOf(observacion.getText());
                            WS_ActualizarInventario ws_actualizarInventario = new WS_ActualizarInventario();
                            ws_actualizarInventario.startWebAccess(c, id_elementoGuardar, serieGuardar, placaGuardar, estadoGuardar, observacionGuardar);

                            handler.post(createUI);
                        }
                    };

                    thread_actualizarregistro.start();
                }
            }
        });

        LlenarListas estadoList = new LlenarListas();
        estadoList.llenarSpinnerEstado1(c, estadoSpin);

        estadoSpin.setSelection(estado);

        estado= 0;

    }
    final Runnable createUI = new Runnable() {

        public void run() {
            Toast.makeText(c, "Se ha actualizado el estado del elemento asignado", Toast.LENGTH_LONG).show();
            WS_Placa cerrar = new WS_Placa();
            cerrar.cerrarDialog();
        }
    };

    final Runnable createESTADO = new Runnable() {

        public void run() {

        }
    };


}