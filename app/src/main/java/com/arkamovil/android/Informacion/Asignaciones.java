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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_ActualizarInventario;
import com.arkamovil.android.servicios_web.WS_Asignaciones;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_RegistroActaVisita;

public class Asignaciones extends Dialog {


    private Activity c;
    private View vista;
    private int i;
    private EditText elemento;
    private EditText placa;
    private EditText estadoAct;
    private EditText estado;
    private EditText observacion;

    private WS_Asignaciones datos;

    private Thread thread_actualizarregistro;

    private Handler handler = new Handler();

    public Asignaciones(Activity a, int i) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.i = i;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_asignaciones);

        elemento = (EditText) findViewById(R.id.elemento_c4);
        placa = (EditText) findViewById(R.id.placa_c4);
        estadoAct = (EditText) findViewById(R.id.estadoactualizacion_c4);
        estado = (EditText) findViewById(R.id.estado_c4);
        observacion = (EditText) findViewById(R.id.observacion_c4);

        datos = new WS_Asignaciones();
        elemento.setText(datos.getId_elemento().get(0));
        placa.setText(datos.getPlaca().get(0));
        estado.setText(datos.getEstado().get(0));
        observacion.setText(datos.getObservaciones().get(0));

        Button cancelar;
        cancelar = (Button) findViewById(R.id.cancelar_c4);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaModificarInventario cerrarDialog = new TablaModificarInventario();
                cerrarDialog.cerrarDialog();
            }
        });

        Button guardar;
        guardar = (Button) findViewById(R.id.guardar_c4);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    thread_actualizarregistro = new Thread() {
                        public void run() {
//                            String id_elementoGuardar = datos.getId_elemento().get(i);
//                            String elementoGuardar = String.valueOf(elemento.getText());
//                            String placaGuardar = String.valueOf(placa.getText());
//                            String serieGuardar = String.valueOf(serie.getText());
//                            String estadoGuardar = String.valueOf(estadoSpin.getSelectedItem());
//                            String observacionGuardar = String.valueOf(observacion.getText());
//                            WS_ActualizarInventario ws_actualizarInventario = new WS_ActualizarInventario();
//                            ws_actualizarInventario.startWebAccess(c, id_elementoGuardar, serieGuardar, placaGuardar,estadoGuardar,observacionGuardar);

                            handler.post(createUI);
                        }
                    };

                    thread_actualizarregistro.start();
                }
        });

    }

    final Runnable createUI = new Runnable() {

        public void run() {
            Toast.makeText(c, "Se ha Actualizado el inventario Asignado", Toast.LENGTH_LONG).show();
            TablaModificarInventario cerrarDialog = new TablaModificarInventario();
            cerrarDialog.cerrarDialog();

        }
    };

}