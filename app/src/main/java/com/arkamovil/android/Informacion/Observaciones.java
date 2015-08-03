package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.servicios_web.WS_ElementosInventario;
import com.arkamovil.android.servicios_web.WS_GuardarObservaciones;
import com.arkamovil.android.servicios_web.WS_Imagen;
import com.arkamovil.android.servicios_web.WS_Observaciones;

public class Observaciones extends Dialog {


    private Activity c;

    private WS_Observaciones datos;
    WS_ElementosInventario elementos;

    private String id_elemento;
    private String id_levantamiento;
    private String funcionario;
    private  int indexObservacion;
    private String getId_levantamientoGenerado;


    private Thread thread;
    private Handler handler = new Handler();

    public Observaciones(Activity a, WS_Observaciones observaciones, WS_ElementosInventario elementos, String funcionario, int indexObservacion) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;

        this.datos = observaciones;
        this.elementos = elementos;

        this.id_elemento = elementos.getId_elemento().get(indexObservacion);
        this.funcionario = funcionario;
        this.indexObservacion = indexObservacion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_observaciones);
Log.v("elemento", id_elemento);
        Button cerrar = (Button) findViewById(R.id.cerrar_obser);
        Button guardar = (Button) findViewById(R.id.guardar_obser);

        EditText obs_fun = (EditText) findViewById(R.id.obser_funcionario);
        final EditText observaciones_almacen = (EditText) findViewById(R.id.observaciones_almacen);
        final EditText obs_almacen = (EditText) findViewById(R.id.obser_almacen);

        final Spinner tipo_movimiento = (Spinner) findViewById(R.id.tipo_movimiento);
        String[] opciones = {"Seleccione ...", "Faltante por hurto", "Faltante por dependencia", "Traslado", "Baja"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c.getApplication(), R.layout.spinner_item, opciones);
        tipo_movimiento.setAdapter(adapter);

        for(int i = 0; i<datos.getId_levantamiento().size(); i++) {
            if("0".equals(datos.getCreador_observacion().get(i))){
                obs_fun.setText(obs_fun.getText() + datos.getFecha_registro().get(i) + ", " + datos.getObservacion().get(i)+ "\n");
            }else if ("1".equals(datos.getCreador_observacion().get(i))){
                String movimiento = "";
                if(!"".equals(datos.getTipo_movimiento().get(i))){
                    if("0".equals(datos.getTipo_movimiento().get(i))){
                        movimiento= "Faltante por hurto";
                    }else if ("1".equals(datos.getTipo_movimiento().get(i))){
                        movimiento= "Faltante por dependencia";
                    }else if ("2".equals(datos.getTipo_movimiento().get(i))){
                        movimiento= "Traslado";
                    }else if ("3".equals(datos.getTipo_movimiento().get(i))){
                        movimiento= "Baja";
                    }
                }
                observaciones_almacen.setText(observaciones_almacen.getText() + datos.getFecha_registro().get(i) + ", " + datos.getObservacion().get(i)+ ", Posible movimiento: " + movimiento +"\n");
            }
        }

        if(datos.getId_levantamiento().size()==0){
            obs_fun.setText("Sin observaciones");
            observaciones_almacen.setText("Sin observaciones");
        }


         cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
         });

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thread = new Thread() {
                        public void run() {
                            if (!"".equals(obs_almacen.getText()) || tipo_movimiento.getSelectedItemPosition() > 0) {
                                WS_GuardarObservaciones ws_guardarObservaciones = new WS_GuardarObservaciones();
                                getId_levantamientoGenerado = ws_guardarObservaciones.startWebAccess(id_elemento, id_levantamiento, funcionario, String.valueOf(obs_almacen.getText()), String.valueOf(tipo_movimiento.getSelectedItemPosition() - 1));
                                handler.post(createUI);
                            }
                        }
                    };

                    thread.start();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(c, "De clic en el botón \"CERRAR\" cuando este activo", Toast.LENGTH_LONG).show();
//    }

    final Runnable createUI = new Runnable() {

        public void run() {
            if("".equals(id_levantamiento)){
                //elementos.setId_levantamiento(indexObservacion, getId_levantamientoGenerado);
            }
            dismiss();
        }
    };
}