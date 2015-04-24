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
    import com.arkamovil.android.procesos.LlenarListas;
    import com.arkamovil.android.procesos.TablaConsultarInventario;
    import com.arkamovil.android.procesos.TablaModificarInventario;
    import com.arkamovil.android.servicios_web.WS_Elemento;

    public class Modificar_Informacion_Elementos  extends Dialog {


        public Activity c;
        public Dialog d;
        public Button cerrar;
        public int i;

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
            setContentView(R.layout.dg_modificar_informacion_elementos);

            TextView elemento = (TextView) findViewById(R.id.elemento_61);
            TextView placa = (TextView) findViewById(R.id.placa_61);
            TextView serie = (TextView) findViewById(R.id.serial_61);

            Spinner estadoSpin = (Spinner) findViewById(R.id.estado_61);

            WS_Elemento datos = new WS_Elemento();

            elemento.setText(datos.getDescripcion().get(i));
            placa.setText(datos.getNivel().get(i));
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

            LlenarListas estado = new LlenarListas();
            estado.llenarSpinnerEstado(c, estadoSpin);
        }

    }