package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.CrearTablas;
import com.arkamovil.android.servicios_web.WS_Elemento;

public class Informacion_Elementos extends Dialog {


    public Activity c;
    public Dialog d;
    public Button cerrar;
    public int i;

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

        WS_Elemento datos = new WS_Elemento();

        id.setText(id.getText() +  "            " + datos.getId_elemento().get(i));
        nivel.setText(nivel.getText() +  "       " + datos.getNivel().get(i));
        marca.setText(marca.getText() +  "     " + datos.getMarca().get(i));
        placa.setText(placa.getText() +  "      " + datos.getPlaca().get(i));
        serie.setText(serie.getText() +  "       " + datos.getSerie().get(i));
        valor.setText(valor.getText() +  "       " + datos.getValor().get(i));
        subtotal.setText(subtotal.getText() +  "  " + datos.getSubtotal().get(i));
        iva.setText(iva.getText() +  "           " + datos.getIva().get(i));
        total.setText(total.getText() +  "        " + datos.getTotal().get(i));

        Button cerrar;
        cerrar = (Button) findViewById(R.id.cerrar_infoelem);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearTablas cerrarDialog = new CrearTablas();
                cerrarDialog.cerrarDialog();
            }
        });
    }

}