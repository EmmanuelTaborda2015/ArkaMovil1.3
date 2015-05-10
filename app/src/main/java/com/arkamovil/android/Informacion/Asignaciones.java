package com.arkamovil.android.Informacion;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.CasoUso4;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.GenerarPDF_Inventarios;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaConsultarInventariosAsignados;
import com.arkamovil.android.procesos.TablaModificarInventario;
import com.arkamovil.android.servicios_web.WS_ActualizarInventario;
import com.arkamovil.android.servicios_web.WS_Asignaciones;
import com.arkamovil.android.servicios_web.WS_Elemento;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
import com.arkamovil.android.servicios_web.WS_RegistroActaVisita;

import java.util.ArrayList;
import java.util.List;

public class Asignaciones extends Dialog {


    private Activity c;

    private AutoCompleteTextView funcionario;

    private EditText elemento;
    private EditText placa;
    private EditText estadoAct;
    private EditText estado;
    private EditText observacion;

    private Button modificar;
    private Button cerrar;

    private int seleccion2;

    private WS_Asignaciones datos;

    public Asignaciones(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_asignaciones);

        funcionario = (AutoCompleteTextView) findViewById(R.id.funcionario_c4_dl);
        elemento = (EditText) findViewById(R.id.elemento_c4);
        placa = (EditText) findViewById(R.id.placa_c4);
        estadoAct = (EditText) findViewById(R.id.estadoactualizacion_c4);
        estado = (EditText) findViewById(R.id.estado_c4);
        observacion = (EditText) findViewById(R.id.observacion_c4);


        funcionario.setEnabled(false);

        modificar= (Button) findViewById(R.id.modificar_c4);
        cerrar = (Button) findViewById(R.id.cancelar_c4);

        datos = new WS_Asignaciones();

        final CasoUso4 casoUso4 = new CasoUso4();

        funcionario.setText(casoUso4.getString_funcionario());
        elemento.setText(datos.getId_elemento().get(0));
        placa.setText(datos.getPlaca().get(0));
        estado.setText(datos.getEstado().get(0));
        estadoAct.setText(datos.getEstado_Actualizacion().get(0));
        observacion.setText(datos.getObservaciones().get(0));

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaConsultarInventariosAsignados cerrarDialog = new TablaConsultarInventariosAsignados();
                cerrarDialog.cerrarDialog();
            }
        });


        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("MODIFICAR".equalsIgnoreCase(String.valueOf(modificar.getText()))){
                    funcionario.setEnabled(true);
                    funcionario.requestFocus();
                    funcionario.setText("");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, casoUso4.getLista_funcionario());
                    funcionario.setAdapter(adapter);

                    modificar.setText("Guardar");
                    cerrar.setText("Cancelar");

                    new Despliegue(funcionario);
                }else if("GUARDAR".equalsIgnoreCase(String.valueOf(modificar.getText()))){

                }

            }
        });

        funcionario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < casoUso4.getLista_funcionario().size(); i++) {
                    if (String.valueOf(funcionario.getText()).equals(casoUso4.getLista_funcionario().get(i))) {
                        seleccion2 = i;
                    }
                }

                String documento = casoUso4.getLista_documentos().get(seleccion2);
                Log.v("Aqui Emmanuel", documento);

            }
        });


    }

}