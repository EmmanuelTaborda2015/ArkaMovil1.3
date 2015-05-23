package com.arkamovil.android.menu_desplegable;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;

import com.arkamovil.android.R;
import com.arkamovil.android.casos_uso.AsociarImagen;
import com.arkamovil.android.casos_uso.CasoUso1;
import com.arkamovil.android.casos_uso.CasoUso2;
import com.arkamovil.android.casos_uso.CasoUso4;
import com.arkamovil.android.casos_uso.CasoUso5;
import com.arkamovil.android.casos_uso.CasoUso6;
import com.arkamovil.android.casos_uso.ConsultaRapida;


public class CasosUso extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    int cont = 0;

    private Button scanear;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;


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
                if (cont == 1) {
                    fragment = new CasoUso1();
                    cont = 0;
                }
                cont++;
                break;
            case 2:
                fragment = new CasoUso2();
                break;
            case 3:
                fragment = new CasoUso4();
                break;
            case 4:
                fragment = new CasoUso5();
                break;
            case 5:
                fragment = new CasoUso6();
                break;
            case 6:
                fragment = new AsociarImagen();
                break;
            case 7:
                fragment = new ConsultaRapida();
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