package com.arkamovil.android.procesos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.arkamovil.android.Login;

/**
 * Created by emmanuel on 13/09/15.
 */
public class FinalizarSesion {

    public static void cerrarSesion(final Activity actividad){

        AlertDialog.Builder dialogo = new AlertDialog.Builder(actividad);

        dialogo.setTitle("SESIÓN CADUCADA");
        dialogo.setMessage("La sesión ha caducado, Por favor inicie sesión nuevamente ");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(actividad, Login.class);
                actividad.startActivity(i);
            }
        });
        dialogo.setCancelable ( false);
        dialogo.create();
        dialogo.show();
    }
}
