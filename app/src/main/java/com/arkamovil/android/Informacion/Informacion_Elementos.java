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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.TablaConsultarInventario;
import com.arkamovil.android.servicios_web.WS_CargarImagen;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Imagen;

public class Informacion_Elementos extends Dialog {


    public Activity c;
    public int i;
    WS_Elemento datos;
    private Thread thread;
    private Handler handler = new Handler();
    private String img;

    public Informacion_Elementos(Activity a, int i) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.i = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_informacion_elemento);

        TextView id = (TextView) findViewById(R.id.infor_id);
        TextView nivel = (TextView) findViewById(R.id.info_nivel);
        TextView marca = (TextView) findViewById(R.id.info_marca);
        TextView placa = (TextView) findViewById(R.id.info_placa);
        TextView serie = (TextView) findViewById(R.id.info_serie);
        TextView valor = (TextView) findViewById(R.id.info_valor);
        TextView subtotal = (TextView) findViewById(R.id.info_subtotal);
        TextView iva = (TextView) findViewById(R.id.info_iva);
        TextView total = (TextView) findViewById(R.id.info_total);

        datos = new WS_Elemento();

        id.setText(datos.getId_elemento().get(i));
        nivel.setText(datos.getNivel().get(i));
        marca.setText(datos.getMarca().get(i));
        placa.setText(datos.getPlaca().get(i));
        serie.setText(datos.getSerie().get(i));
        valor.setText(datos.getValor().get(i));
        subtotal.setText(datos.getSubtotal().get(i));
        iva.setText(datos.getIva().get(i));
        total.setText(datos.getTotal().get(i));

        thread = new Thread() {
            public void run() {

                WS_Imagen ws_imagen = new WS_Imagen();
                img =  ws_imagen.startWebAccess(datos.getId_elemento().get(i));

                handler.post(createUI);
            }
        };

        thread.start();

        Button cerrar;
        cerrar = (Button) findViewById(R.id.cerrar_infoelem);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaConsultarInventario cerrarDialog = new TablaConsultarInventario();
                cerrarDialog.cerrarDialog();
            }
        });
    }

    final Runnable createUI = new Runnable() {

        public void run() {

            byte[] byteData = Base64.decode(img, Base64.DEFAULT);
            Bitmap photo = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);

            ImageView imagen = (ImageView) findViewById(R.id.imagen_inf);
            imagen.setImageBitmap(photo);
        }
    };
}