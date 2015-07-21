package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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
import com.arkamovil.android.servicios_web.WS_Imagen;

public class Observaciones extends Dialog {


    private Activity c;
    private int i;
    private Thread thread;
    private Handler handler = new Handler();
    private String img;
    private Button cerrar;
    private WS_ElementosInventario datos;

    public Observaciones(Activity a, int i) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.i = i;
        this.datos = datos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_observaciones);

        Button cerrar = (Button) findViewById(R.id.cerrar_obser);
        Button guardar = (Button) findViewById(R.id.guardar_obser);

        EditText obs_fun = (EditText) findViewById(R.id.obser_funcionario);
        EditText obs_almacen = (EditText) findViewById(R.id.obser_almacen);

        Spinner tipo_movimiento=(Spinner) findViewById(R.id.tipo_movimiento);
        String []opciones={"Seleccione ...", "Faltante por hurto", "Faltante por dependencia", "Traslado", "Baja"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c.getApplication(),R.layout.spinner_item, opciones);
        tipo_movimiento.setAdapter(adapter);

//        thread = new Thread() {
//            public void run() {
//                WS_Imagen ws_imagen = new WS_Imagen();
//                img = ws_imagen.startWebAccess(datos.getId_elemento().get(i));
//                handler.post(createUI);
//            }
//        };
//
//        thread.start();

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(c, "De clic en el botón \"CERRAR\" cuando este activo", Toast.LENGTH_LONG).show();
    }

    final Runnable createUI = new Runnable() {

        public void run() {


        }
    };
}