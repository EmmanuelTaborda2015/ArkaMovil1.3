package com.arkamovil.android.menu_desplegable;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.arkamovil.android.Login;
import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.AsociarImagen;
import com.arkamovil.android.casos_uso.ActaVisita;
import com.arkamovil.android.casos_uso.CriteriosLevantamientoFisico;
import com.arkamovil.android.casos_uso.Radicacion;
import com.arkamovil.android.procesos.FinalizarSesion;
import com.arkamovil.android.servicios_web.WS_CerrarSesion;
import com.arkamovil.android.servicios_web.WS_ValidarSesion;


public class CasosUso extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    int cont = 0;

    private Thread thread_cerrarSesion;
    private Handler handler_cerrarSesion = new Handler();
    private String webResponse_cerrarSesion;

    public static int getSalir() {
        return salir;
    }

    private static int salir = 0;

    private Button scanear;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

   // @Override
    //public void onBackPressed() {
        //Toast.makeText(c, "De clic en el botón \"CERRAR\" cuando este activo", Toast.LENGTH_LONG).show();
    //}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casos_uso);



        this.salir = 1;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    //En esta parte del código se redireccionan a cada uno de los fragmentos según la selección del usuario

    public void onSectionAttached(int number) {
        Fragment fragment = null;
        switch (number) {
            case 1:
                if (cont == 1) {
                    fragment = new ActaVisita();
                    this.setTitle("Acta de Visita");
                    cont = 0;
                }
                cont++;
                break;
            case 2:
                fragment = new CriteriosLevantamientoFisico();
                this.setTitle("Levantamiento Físico de Inventarios");
                break;
//            case 3:
//                fragment = new Radicacion();
//                this.setTitle("Radicación");
//                break;
            case 3:
                fragment = new AsociarImagen();
                this.setTitle("Asociar Imagen a Elemento");
                break;

            case 4:
                this.salir = 1;
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

                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CasosUso) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

    final Runnable cerrarSesion = new Runnable() {

        public void run() {
            if("true".equals(webResponse_cerrarSesion)){
                Intent i = new Intent (CasosUso.this, Login.class) ;
                startActivity(i);
            }
        }
    };
}