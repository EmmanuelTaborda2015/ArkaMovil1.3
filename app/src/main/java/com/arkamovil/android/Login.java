package com.arkamovil.android;

        import android.content.Context;

import android.app.AlertDialog;
import android.content.DialogInterface;
        import android.provider.Settings;
import android.provider.Settings.Secure;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

        import com.arkamovil.android.herramientas.AppStatus;
        import com.arkamovil.android.menu_desplegable.CasosUso;
import com.arkamovil.android.servicios_web.WS_CerrarSesion;
import com.arkamovil.android.servicios_web.WS_Login;
        import com.arkamovil.android.servicios_web.WS_ValidarConexion;

public class Login extends ActionBarActivity {

    private AlertDialog alertaConexion;
    private Context context = this;
    private Thread thread;
    private Thread thread_WS_Fucncionario;
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private String webResponse;

    private EditText usuario;
    private EditText contrasena;
    private Button boton;

    private Thread thread_sesion;
    private Thread thread_cerrarSesion;
    private Handler handler_cerrarSesion = new Handler();
    private Handler handler_conexion = new Handler();
    private String webResponse_cerrarSesion;
    private Thread thread_validarConexion;
    private String webResponse_conexion;

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

            //"0xel0t1l"
            //MCrypt encriptar = new MCrypt();
            //encriptar.encrypt("hola");

       /* BroadcastReceiver broadcastReceiver  =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                    if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                        //do stuff
                        Toast.makeText(getApplicationContext(), "Conectado a Internet!!!!", Toast.LENGTH_LONG).show();
                    } else {
                        // wifi connection was lost
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Sin Conexión a Internet");
                        builder.setMessage("Por Favor Conectese a una red Wi-Fi o Movil");
                        //builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setCancelable(false);
                        alertaConexion = builder.create();
                        alertaConexion.show();

                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
*/

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
                    AppStatus status = new AppStatus();
                    if (status.getInstance(getApplication()).isOnline()) {

                        thread_validarConexion = new Thread() {
                            public void run() {
                                Looper.prepare();
                                WS_ValidarConexion validarConexion = new WS_ValidarConexion();
                                webResponse_conexion = validarConexion.startWebAccess();
                                handler_conexion.post(conexion);
                            }
                        };

                        thread_validarConexion.start();

                        thread_cerrarSesion = new Thread() {
                            public void run() {
                                Looper.prepare();
                                String id_dispositivo = Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
                                WS_CerrarSesion ws_cerrarSesion = new WS_CerrarSesion();
                                webResponse_cerrarSesion = ws_cerrarSesion.startWebAccess(new Login().getUsuarioSesion(), id_dispositivo);
                                handler_cerrarSesion.post(cerrarSesion);
                            }
                        };
                        thread_cerrarSesion.start();
                    } else {

                        // wifi connection was lost
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Sin Conexión a Internet");
                        builder.setMessage("No se ha podido iniciar sesión debido a que no hay conexión a internet. \nPor favor conectese a una red Wi-Fi o Movil e inicie sesión.");
                        builder.setPositiveButton("Entendido",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setCancelable(false);
                        alertaConexion = builder.create();
                        alertaConexion.show();
                        boton.setEnabled(true);

                        boton.setTextColor(getResources().getColor(R.color.NEGRO));
                    }
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
    final Runnable cerrarSesion = new Runnable() {

        public void run() {

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

        }
    };

    final Runnable conexion = new Runnable() {

        public void run() {
            if("false".equals(webResponse_conexion)){
                // wifi connection was lost
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Problemas en la Conexión a Internet");
                builder.setMessage("La conexión a internet mediante la cual esta tratando de acceder no es valida. \nPor favor verifique la configuración del proxy o intente nuevamente conectandose a otra red Wi-Fi o Movil.");
                builder.setPositiveButton("Entendido",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setCancelable(false);
                alertaConexion = builder.create();
                alertaConexion.show();
            }
        }
    };

}