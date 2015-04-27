package com.arkamovil.android;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arkamovil.android.menu_desplegable.CasosUso;
import com.arkamovil.android.servicios_web.WS_Login;

public class Login extends ActionBarActivity {

    private Thread thread;
    private Handler handler = new Handler();
    private String webResponse;

    private EditText usuario;
    private EditText contrasena;
    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText)findViewById(R.id.user);
        contrasena = (EditText)findViewById(R.id.contrasenna_usuario);

        boton = (Button)findViewById(R.id.botonlogin);

        boton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {


                boton.setEnabled(false);

                int contador = 0;
                //Se realizan validaciones previas antes de consultar la base de datos.
                if("".equals(String.valueOf(usuario.getText())) || "Usuario".equals(String.valueOf(usuario.getText()))) {
                    Toast.makeText(getApplicationContext(), "Porfavor ingrese su usuario", Toast.LENGTH_LONG).show();
                    contador++;
                }
                if(("".equals(String.valueOf(contrasena.getText())) || "Contraseña".equals(String.valueOf(contrasena.getText()))) && contador == 0){
                    Toast.makeText(getApplicationContext(), "Porfavor ingrese su contraseña", Toast.LENGTH_LONG).show();
                    contador++;
                }
                if(contador == 0){

                    thread = new Thread() {
                        public void run() {
                            //Se crea el objeto de la clase (Web Service Login), y se le envian los parametros Context, usuario y contraseña ingresadas.
                            WS_Login verificar = new WS_Login();
                            webResponse = verificar.startWebAccess(String.valueOf(usuario.getText()), String.valueOf(contrasena.getText()));
                            handler.post(createUI);
                        }
                    };

                    thread.start();

                }else{
                    boton.setEnabled(true);
                }
            }
        });

        usuario.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                                             public void onFocusChange(View v, boolean hasFocus){
                                                 if (hasFocus) {
                                                     try {
                                                         usuario.setText("");
                                                         usuario.setTextColor(getResources().getColor(R.color.NEGRO));
                                                     }
                                                     catch (NumberFormatException e) {
                                                     }
                                                 }
                                             }
                                         }
        );

        contrasena.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                                                public void onFocusChange(View v, boolean hasFocus){
                                                    if (hasFocus) {
                                                        try {
                                                            contrasena.setText("");
                                                            contrasena.setTextColor(getResources().getColor(R.color.NEGRO));
                                                        }
                                                        catch (NumberFormatException e) {
                                                        }
                                                    }
                                                }
                                            }
        );
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            //if("true".equals(webResponse)){
            if("true".equals("true")){
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
                boton.setEnabled(true);
                Intent i = new Intent(getApplicationContext(), CasosUso.class);
                startActivity(i);

            }else if("false".equals(webResponse)){
                Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña invalida", Toast.LENGTH_LONG).show();
                boton.setEnabled(true);

            }
        }
    };
}