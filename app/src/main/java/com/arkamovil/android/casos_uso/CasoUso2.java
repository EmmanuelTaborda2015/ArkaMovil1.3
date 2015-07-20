package com.arkamovil.android.casos_uso;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.TablaAsignarInventarios;
import com.arkamovil.android.procesos.TablaConsultarInventario;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento_dependencia;
import com.arkamovil.android.servicios_web.WS_Elemento_funcionario;
import com.arkamovil.android.servicios_web.WS_ElementosAsignar;
import com.arkamovil.android.servicios_web.WS_EnviarElementosAsignar;
import com.arkamovil.android.servicios_web.WS_Funcionario;
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
import com.arkamovil.android.servicios_web.WS_InventarioTipoConfirmacion;
import com.arkamovil.android.servicios_web.WS_Sede;
import com.arkamovil.android.servicios_web.WS_Ubicacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CasoUso2 extends Fragment {

    private AutoCompleteTextView sede;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView funcionario;

    private static String string_sede;
    private static String string_funcionario;

    private Thread thread;
    private Handler handler = new Handler();

    private List<String> lista_sede = new ArrayList<String>();
    private List<String> lista_id_sede = new ArrayList<String>();

    private List<String> lista_dependencia = new ArrayList<String>();
    private List<String> lista_id_dependencia = new ArrayList<String>();

    private static List<String> lista_funcionario = new ArrayList<String>();
    private List<String> lista_documento = new ArrayList<String>();

    private ImageView bajar;
    private ImageView subir;
    private Button asignar;

    private WS_ElementosAsignar elem;

    private static View rootView;


    private int seleccion = -1;
    private int seleccion1 = -1;
    private int seleccion2 = -1;
    private int seleccion3 = -1;

    private  int estado_aprob = -1;


    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_asignar_inventarios, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c2);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_c2);
        funcionario = (AutoCompleteTextView) rootView.findViewById(R.id.funcionario_c2);

        bajar = (ImageView) rootView.findViewById(R.id.bajar_c2);
        subir = (ImageView) rootView.findViewById(R.id.subir_c2);

        dependencia.setEnabled(false);

        asignar = (Button) rootView.findViewById(R.id.asignar_c2);
        asignar.setVisibility(View.GONE);

        bajar.setVisibility(View.GONE);
        subir.setVisibility(View.GONE);

        final Button des1 = (Button) rootView.findViewById(R.id.filtro_tod_c2);
        final Button des2 = (Button) rootView.findViewById(R.id.filtro_dep_c2);
        final Button des3 = (Button) rootView.findViewById(R.id.filtro_fun_c2);

        des1.setVisibility(View.GONE);
        des2.setVisibility(View.GONE);
        des3.setVisibility(View.GONE);

        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.datos_todos);
        l1.setVisibility(View.GONE);

        final LinearLayout l2 = (LinearLayout) rootView.findViewById(R.id.datos_funcionario);
        l2.setVisibility(View.GONE);

        final LinearLayout l3 = (LinearLayout) rootView.findViewById(R.id.filtro);
        l3.setVisibility(View.GONE);



        Spinner tipo_confirmacion=(Spinner) rootView.findViewById(R.id.tipo_confirmacion);
        String []opciones={"Seleccione ...", "Aprobados","No Aprobados","Sin Verificar"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item, opciones);
        tipo_confirmacion.setAdapter(adapter);

        Spinner criterio_busqueda=(Spinner) rootView.findViewById(R.id.criterios_busqueda);
        String []opciones2={"Seleccione ...", "Todos", "Sede y/o Dependencia", "Funcionario"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item, opciones2);
        criterio_busqueda.setAdapter(adapter2);


        tipo_confirmacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado_aprob = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        criterio_busqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( i == 1 ){
                    des1.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);

                    des2.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);

                    des3.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                }else if( i== 2){
                    des1.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);

                    des2.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);

                    des3.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                }else if( i== 3){
                    des1.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);

                    des2.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);

                    des3.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        WS_Sede ws_sede = new WS_Sede();
        ws_sede.startWebAccess(getActivity(), sede);

        lista_sede = ws_sede.getSede();
        lista_id_sede = ws_sede.getId_sede();


        WS_Funcionario ws_funcionario = new WS_Funcionario();
        ws_funcionario.startWebAccess(getActivity(), funcionario, "null");

        lista_funcionario = ws_funcionario.getFun_nombre();
        lista_documento = ws_funcionario.getFun_identificacion();

        new Despliegue(funcionario);

        des1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();

                if (on) {
                    l1.setVisibility(View.VISIBLE);
                } else {
                    l1.setVisibility(View.GONE);
                }
            }
        });

        des2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    l2.setVisibility(View.VISIBLE);
                } else {
                    l2.setVisibility(View.GONE);
                }
            }
        });

        des3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    l3.setVisibility(View.VISIBLE);
                } else {
                    l3.setVisibility(View.GONE);
                }
            }
        });


        new Despliegue(sede);

        sede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lista_sede.size(); i++) {
                    if (String.valueOf(sede.getText()).equals(lista_sede.get(i))) {
                        seleccion = i;
                    }
                }
                //Se envia parametros de vista y de campo AutoComplete al web service de facultad.
                WS_Dependencia ws_dependencia = new WS_Dependencia();
                ws_dependencia.startWebAccess(getActivity(), dependencia, lista_id_sede.get(seleccion));

                lista_dependencia = ws_dependencia.getDependencia();
                lista_id_dependencia = ws_dependencia.getId_dependencia();

                string_sede = String.valueOf(sede.getText());

                dependencia.setText("");
                dependencia.requestFocus();

                //Se despliegan los datos obtenidos de la dependencia.
                new Despliegue(dependencia);
            }
        });

        dependencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        //

        final Button consultar_tod = (Button) rootView.findViewById(R.id.con_tod_c2);
        consultar_tod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( estado_aprob > 0 ){
//                    limpiarTabla();
//
                    //WS_InventarioTipoConfirmacion elem2 = new WS_InventarioTipoConfirmacion();
                    //elem2.startWebAccess(rootView, getActivity());

                    Fragment fragment = new LevantamientoFisico();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                Toast.makeText(getActivity(), "Por favor seleccione el estado de aprobación a consultar.", Toast.LENGTH_LONG).show();
            }

        }
    });
        final Button consultar_dep = (Button) rootView.findViewById(R.id.con_dep_c2);
        consultar_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( estado_aprob > 0 ){
                    seleccion = -1;
                    seleccion1 = -1;
                    seleccion2 = -1;

                    for (int i = 0; i < lista_sede.size(); i++) {
                        if (String.valueOf(sede.getText()).equalsIgnoreCase(lista_sede.get(i))) {
                            seleccion = i;
                        }
                    }

                    if (seleccion > -1) {
                        if(!String.valueOf(dependencia.getText()).equalsIgnoreCase("")){
                            string_sede = String.valueOf(sede.getText());
                            for (int i = 0; i < lista_dependencia.size(); i++) {
                                if (String.valueOf(dependencia.getText()).equalsIgnoreCase(lista_dependencia.get(i))) {
                                    seleccion1 = i;
                                }
                            }
                            if (seleccion1 > -1) {
                                limpiarTabla();

                                elem = new WS_ElementosAsignar();
                                elem.startWebAccess(rootView, getActivity(), "12/06/2015", "17/08/2015");
                            } else {
                                Toast.makeText(getActivity(), "La Dependencia ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                                asignar.setEnabled(true);
                            }
                        }else{
                            limpiarTabla();

                            elem = new WS_ElementosAsignar();
                            elem.startWebAccess(rootView, getActivity(), "12/06/2015", "17/08/2015");
                        }
                    } else {
                        Toast.makeText(getActivity(), "La Sede ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                        asignar.setEnabled(true);
                    }

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }else{
                    Toast.makeText(getActivity(), "Por favor seleccione el estado de aprobación a consultar.", Toast.LENGTH_LONG).show();
                }

            }
        });


        final Button consultar_fun = (Button) rootView.findViewById(R.id.con_fun_c2);
        consultar_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( estado_aprob > 0 ){

                for (int i = 0; i < lista_funcionario.size(); i++) {
                    if (String.valueOf(funcionario.getText()).equalsIgnoreCase(lista_documento.get(i) + " - " +lista_funcionario.get(i))) {
                        seleccion3 = i;
                    }
                }
                if (seleccion3 > -1) {
                    limpiarTabla();

                    elem = new WS_ElementosAsignar();
                    elem.startWebAccess(rootView, getActivity(), "12/06/2015", "17/08/2015");
                } else {
                    Toast.makeText(getActivity(), "El funcionario ingresado no es valido, por favor verifiquelo e intente de nuevo.", Toast.LENGTH_LONG).show();
                    asignar.setEnabled(true);
                }
                }else{
                    Toast.makeText(getActivity(), "Por favor seleccione el estado de aprobación a consultar.", Toast.LENGTH_LONG).show();
                }
            }

        });

        funcionario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });

        //boton para bajar los elementos
        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaAsignarInventarios baj = new TablaAsignarInventarios();
                baj.bajar(rootView, getActivity());
            }
        });
        //llamar metodo en clase CrearTablas para ir hacia los primeros registros mostrados en la tabla
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablaAsignarInventarios sub = new TablaAsignarInventarios();
                sub.subir(rootView, getActivity());
            }
        });

//        final Button consultar = (Button) rootView.findViewById(R.id.con_dep_c2);
//        consultar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                limpiarTabla();
//
//                elem = new WS_ElementosAsignar();
//                elem.startWebAccess(rootView, getActivity(), "12/06/2015", "17/08/2015");

//                consultar.setEnabled(false);
//                if ("DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha1.getText())) || "DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha2.getText()))) {
//                    Toast.makeText(getActivity(), "Ingrese la fecha inicial y/o la fecha final", Toast.LENGTH_LONG).show();
//                    consultar.setEnabled(true);
//                } else {
//                    limpiarTabla();
//
//                    elem = new WS_ElementosAsignar();
//                    elem.startWebAccess(rootView, getActivity(), "12/06/2015", "17/08/2015");
////                }
//            }
//        });

        //fecha1 = (TextView) rootView.findViewById(R.id.fecha1_c2);
        //fecha1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                contador = 0;
//
//                Time today = new Time(Time.getCurrentTimezone());
//                today.setToNow();
//
//                String val1 = "";
//                String val2 = "";
//
//                if (today.monthDay < 10) {
//                    val1 = "0";
//                }
//
//                if ((today.month + 1) < 10) {
//                    val2 = "0";
//                }
//
//                year1 = today.year;
//                month1 = today.month;
//                day1 = today.monthDay;
//
//                DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
//                        year1, month1, day1);
//                dialog.show();
//
//            }
//        });
//
//        fecha2 = (TextView) rootView.findViewById(R.id.fecha2_c2);
//        fecha2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                contador = 1;
//
//                final Calendar c = Calendar.getInstance();
//                year2 = c.get(Calendar.YEAR);
//                month2 = c.get(Calendar.MONTH);
//                day2 = c.get(Calendar.DAY_OF_MONTH);
//
//
//                if (!"DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha1.getText()))) {
//                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
//                            year1, month1, day1);
//                    dialog.show();
//                } else {
//                    Toast.makeText(getActivity(), "Por favor ingrese primero la fecha inicial.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        return rootView;
    }

    public void limpiarTabla() {

        TablaAsignarInventarios borrar = new TablaAsignarInventarios();
        borrar.borrarTabla(rootView, getActivity());
        asignar.setVisibility(View.GONE);
        bajar.setVisibility(View.GONE);
        subir.setVisibility(View.GONE);
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            asignar.setVisibility(View.GONE);
            limpiarTabla();
            elem = new WS_ElementosAsignar();
            elem.startWebAccess(rootView, getActivity(), "15/04/1988", "15/04/2016");
            string_funcionario = String.valueOf(funcionario.getText());
            Toast.makeText(getActivity(), "Han sido asignados los elementos.", Toast.LENGTH_LONG).show();

            seleccion = -1;
            seleccion1 = -1;
            seleccion2 = -1;

            asignar.setEnabled(true);

        }
    };

    public static String getString_sede() {
        return string_sede;
    }

    public static String getString_funcionario() {
        return string_funcionario;
    }

    public static List<String> getLista_funcionario() {
        return lista_funcionario;
    }
}

