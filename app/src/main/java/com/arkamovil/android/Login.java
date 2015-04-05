package com.arkamovil.android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arkamovil.android.menu_desplegable.CasosUso;

public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button boton = (Button)findViewById(R.id.botonlogin);
        boton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CasosUso.class );
                startActivity(i);
            }
        });
    }
}
