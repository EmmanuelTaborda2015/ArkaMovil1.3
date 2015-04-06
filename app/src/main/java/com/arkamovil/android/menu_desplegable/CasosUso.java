package com.arkamovil.android.menu_desplegable;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.CasoUso1;
import com.arkamovil.android.casos_uso.CasoUso4;
import com.arkamovil.android.casos_uso.CasoUso5;


public class CasosUso extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    int cont = 0;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;


    //función que captura el evento scanner del fragmen fm_casouso4, que inicia la captura del código de barras

    public void escanear(View v){
        Intent intent = new Intent("com.arkamovil.android.SCAN");
        startActivityForResult(intent, 0);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //En esta función se llama a la libreria encargada de leer y obtener la información de los códigos de barras.

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contenido = intent.getStringExtra("SCAN_RESULT");
                String formato = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Hacer algo con los datos obtenidos.

                Toast.makeText(getApplicationContext(), contenido, Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                // Si se cancelo la captura.
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casos_uso);

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
                if(cont==1){
                    fragment = new CasoUso1();
                    cont = 0;
                }
                cont++;
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                fragment = new CasoUso4();
                break;
            case 5:
                fragment = new CasoUso5();
            case 6:
                mTitle = getString(R.string.title_section3);
                break;
            case 7:
                mTitle = getString(R.string.title_section3);
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

}