package com.arkamovil.android;

import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arkamovil.android.menu_desplegable.CasosUso;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Login;

public class Login extends ActionBarActivity {

    private Thread thread;
    private Thread thread_WS_Fucncionario;
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private String webResponse;

    private EditText usuario;
    private EditText contrasena;
    private Button boton;

    public int getSalir() {
        return salir;
    }

    public void Salir(int salir) {
        this.salir = 1;

    }

    private int salir = 0;

    private ProgressDialog circuloProgreso;

    static public String getUsuarioSesion() {
        return usuarioSesion;
    }

    private static String usuarioSesion = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try{
          salir = CasosUso.getSalir();
        }catch(Exception salida){
        }

        if(salir  == 1){
            System.exit(0);
        }


        usuario = (EditText) findViewById(R.id.user);
        contrasena = (EditText) findViewById(R.id.contrasenna_usuario);

        boton = (Button) findViewById(R.id.botonlogin);

        boton.setTextColor(getResources().getColor(R.color.NEGRO));

        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boton.setEnabled(false);
                boton.setTextColor(getResources().getColor(R.color.BLANCO));

                int contador = 0;
                //Se realizan validaciones previas antes de consultar la base de datos.
                if ("".equals(String.valueOf(usuario.getText())) || "Usuario".equals(String.valueOf(usuario.getText()))) {
                    Toast.makeText(getApplicationContext(), "Porfavor ingrese su usuario", Toast.LENGTH_LONG).show();
                    contador++;
                }
                if (("".equals(String.valueOf(contrasena.getText())) || "Contraseña".equals(String.valueOf(contrasena.getText()))) && contador == 0) {
                    Toast.makeText(getApplicationContext(), "Porfavor ingrese su contraseña", Toast.LENGTH_LONG).show();
                    contador++;
                }
                if (contador == 0) {
                    thread = new Thread() {
                        public void run() {
                            //Se crea el objeto de la clase (Web Service Login), y se le envian los parametros Context, usuario y contraseña ingresadas.
                            String id_dispositivo = Secure.getString(getApplication().getContentResolver(), Secure.ANDROID_ID);
                            WS_Login verificar = new WS_Login();
                            webResponse = verificar.startWebAccess(String.valueOf(usuario.getText()), String.valueOf(contrasena.getText()), id_dispositivo);
                            handler.post(createUI);
                        }
                    };

                    thread.start();

                } else {
                    boton.setEnabled(true);

                    boton.setTextColor(getResources().getColor(R.color.NEGRO));
                }
            }
        });

        usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                             public void onFocusChange(View v, boolean hasFocus) {
                                                 if (hasFocus) {
                                                     try {
                                                         usuario.setText("");
                                                         usuario.setTextColor(getResources().getColor(R.color.NEGRO));
                                                     } catch (NumberFormatException e) {
                                                     }
                                                 }
                                             }
                                         }
        );

        contrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                public void onFocusChange(View v, boolean hasFocus) {
                                                    if (hasFocus) {
                                                        try {
                                                            contrasena.setText("");
                                                            contrasena.setTextColor(getResources().getColor(R.color.NEGRO));
                                                            contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                                        } catch (NumberFormatException e) {
                                                        }
                                                    }
                                                }
                                            }
        );
    }

    final Runnable createUI = new Runnable() {

        public void run() {

            boton.setTextColor(getResources().getColor(R.color.NEGRO));

            if ("true".equals(webResponse)) {
                usuarioSesion = String.valueOf(usuario.getText());
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), CasosUso.class);
                startActivity(i);
            } else if ("false".equals(webResponse)) {
                Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña invalida", Toast.LENGTH_LONG).show();
            }
            boton.setEnabled(true);
        }
    };

}