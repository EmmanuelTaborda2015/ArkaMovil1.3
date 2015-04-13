package com.arkamovil.android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arkamovil.android.menu_desplegable.CasosUso;
import com.arkamovil.android.servicios_web.WS_Login;

public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usuario = (EditText)findViewById(R.id.user);
        final EditText password = (EditText)findViewById(R.id.contrasenna_usuario);

        Button boton = (Button)findViewById(R.id.botonlogin);
        boton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                EditText usuario = (EditText)findViewById(R.id.user);
                EditText contrasena = (EditText)findViewById(R.id.contrasenna_usuario);

                Button boton = (Button)findViewById(R.id.botonlogin);

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
                    //Se crea el objeto de la clase (Web Service Login), y se le envian los parametros Context, usuario y contraseña ingresadas.
                    WS_Login verificar = new WS_Login();
                    verificar.startWebAccess(getApplicationContext(), String.valueOf(usuario.getText()), String.valueOf(contrasena.getText()));
                }else{

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

        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                                             public void onFocusChange(View v, boolean hasFocus){
                                                 if (hasFocus) {
                                                     try {
                                                         password.setText("");
                                                         password.setTextColor(getResources().getColor(R.color.NEGRO));
                                                     }
                                                     catch (NumberFormatException e) {
                                                     }
                                                 }
                                             }
                                         }
        );
    }
}
