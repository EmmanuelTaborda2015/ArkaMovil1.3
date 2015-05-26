package com.arkamovil.android.casos_uso;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.arkamovil.android.servicios_web.WS_Funcionario_Oracle;
import com.arkamovil.android.servicios_web.WS_Sede;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CasoUso2 extends Fragment {

    private AutoCompleteTextView sede;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView funcionario;

    private TextView fecha1;
    private TextView fecha2;

    String fecha_inicio;
    String fecha_final;

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

    private static int year1;
    private static int month1;
    private static int day1;
    private static int year2;
    private static int month2;
    private static int day2;

    private int contador = 0;
    private int validador = 0;

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

        //Se envia parametros de vista y de dependencia seleccionada en el campo AutoComplete al web service de dependencias.

        WS_Sede ws_sede = new WS_Sede();
        ws_sede.startWebAccess(getActivity(), sede);
        lista_sede = ws_sede.getSede();
        lista_id_sede = ws_sede.getId_sede();


        WS_Funcionario_Oracle ws_funcionario = new WS_Funcionario_Oracle();
        ws_funcionario.startWebAccess(getActivity(), funcionario, "null");

        lista_funcionario = ws_funcionario.getFun_nombre();
        lista_documento = ws_funcionario.getFun_identificacion();

        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.datos_funcionario);
        l1.setVisibility(View.GONE);

        final LinearLayout l2 = (LinearLayout) rootView.findViewById(R.id.filtro);
        l2.setVisibility(View.GONE);

        Button des1 = (Button) rootView.findViewById(R.id.des_fun_c2);
        des1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();

                if (on) {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                } else {
                    l1.setVisibility(View.GONE);
                }
            }
        });

        Button des2 = (Button) rootView.findViewById(R.id.fechas_c2);
        des2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    l2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                } else {
                    l2.setVisibility(View.GONE);
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
                ws_dependencia.startWebAccess(getActivity(), dependencia, lista_sede.get(seleccion));

//                WS_Dependencia_Postgres ws_dependencia = new WS_Dependencia_Postgres();
//                ws_dependencia.startWebAccess(getActivity(), dependencia);

                lista_dependencia = ws_dependencia.getDependencia();
                lista_id_dependencia = ws_dependencia.getId_dependencia();

                string_sede = String.valueOf(sede.getText());

                dependencia.setText("");
                dependencia.requestFocus();

                //Se despliegan los datos obtenidos de la dependencia.
                new Despliegue(dependencia);
            }
        });

        //Se genera esta función cuando se selecciona un item de la lista
        dependencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lista_dependencia.size(); i++) {
                    if (String.valueOf(dependencia.getText()).equals(lista_dependencia.get(i))) {
                        seleccion = i;
                    }
                }
                //Se envia parametros de vista y de campo AutoComplete al web service de funcionarios.

//                WS_Funcionario ws_funcionario = new WS_Funcionario();
//                ws_funcionario.startWebAccess(getActivity(), funcionario, lista_dependencia.get(seleccion));


                funcionario.setText("");
                funcionario.requestFocus();

                new Despliegue(funcionario);
            }
        });

        asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asignar.setEnabled(false);

                seleccion = -1;
                seleccion1 = -1;
                seleccion2 = -1;

                for (int i = 0; i < lista_sede.size(); i++) {
                    if (String.valueOf(sede.getText()).equalsIgnoreCase(lista_sede.get(i))) {
                        seleccion = i;
                    }
                }

                if (seleccion > -1) {
                    string_sede = String.valueOf(sede.getText());
                    for (int i = 0; i < lista_dependencia.size(); i++) {
                        if (String.valueOf(dependencia.getText()).equalsIgnoreCase(lista_dependencia.get(i))) {
                            seleccion1 = i;
                        }
                    }
                    if (seleccion1 > -1) {

                        for (int i = 0; i < lista_funcionario.size(); i++) {
                            if (String.valueOf(funcionario.getText()).equalsIgnoreCase(lista_documento.get(i))) {
                                seleccion2 = i;
                            }
                        }
                        if (seleccion2 > -1) {
                            //AQui va el codigo de asignar :)
                            thread = new Thread() {
                                public void run() {

                                    TablaAsignarInventarios selec = new TablaAsignarInventarios();
                                    for (int i = 0; i < elem.getId_elemento().size(); i++) {
                                        if (selec.getArr()[i] == true) {
                                            WS_EnviarElementosAsignar ws_enviarElementosAsignar = new WS_EnviarElementosAsignar();
                                            String a = ws_enviarElementosAsignar.startWebAccess(String.valueOf(lista_id_sede.get(seleccion)), String.valueOf(lista_id_dependencia.get(seleccion1)), String.valueOf(lista_documento.get(seleccion2)), String.valueOf(elem.getId_elemento().get(i)));
                                        }
                                    }

                                    handler.post(createUI);
                                }
                            };

                            thread.start();

                        } else {
                            Toast.makeText(getActivity(), "El funcionario ingresado no es valido, por favor verifiquelo e intente de nuevo.", Toast.LENGTH_LONG).show();
                            asignar.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(getActivity(), "La Dependencia ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                        asignar.setEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "La Sede ingresada no es valida, verifeque e intente de nuevo.", Toast.LENGTH_LONG).show();
                    asignar.setEnabled(true);
                }

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

        final Button consultar = (Button) rootView.findViewById(R.id.con_c2);
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultar.setEnabled(false);
                if ("DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha1.getText())) || "DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha2.getText()))) {
                    Toast.makeText(getActivity(), "Ingrese la fecha inicial y/o la fecha final", Toast.LENGTH_LONG).show();
                    consultar.setEnabled(true);
                } else {
                    limpiarTabla();

                    elem = new WS_ElementosAsignar();
                    elem.startWebAccess(rootView, getActivity(), fecha_inicio, fecha_final);
                }
            }
        });

        fecha1 = (TextView) rootView.findViewById(R.id.fecha1_c2);
        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contador = 0;

                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                String val1 = "";
                String val2 = "";

                if (today.monthDay < 10) {
                    val1 = "0";
                }

                if ((today.month + 1) < 10) {
                    val2 = "0";
                }

                year1 = today.year;
                month1 = today.month;
                day1 = today.monthDay;

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                        year1, month1, day1);
                dialog.show();

            }
        });

        fecha2 = (TextView) rootView.findViewById(R.id.fecha2_c2);
        fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                contador = 1;

                final Calendar c = Calendar.getInstance();
                year2 = c.get(Calendar.YEAR);
                month2 = c.get(Calendar.MONTH);
                day2 = c.get(Calendar.DAY_OF_MONTH);


                if (!"DD/MM/AAAA".equalsIgnoreCase(String.valueOf(fecha1.getText()))) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                            year1, month1, day1);
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Por favor ingrese primero la fecha inicial.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    public boolean verificarFechas() {

        boolean verificar = false;

        if (year1 <= year2) {
            if (month1 <= month2) {
                if (day1 <= day2) {
                    verificar = true;
                }
            }
        }
        return verificar;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {


            if (contador == 0) {

                month1 = selectedMonth;
                day1 = selectedDay;
                year1 = selectedYear;

                String val1 = "";
                String val2 = "";

                if (day1 < 10) {
                    val1 = "0";
                }

                if ((month1 + 1) < 10) {
                    val2 = "0";
                }

                fecha_inicio = val1 + day1 + "/" + val2 + (month1 + 1) + "/" + year1;

                fecha1.setText(fecha_inicio);
                fecha1.setTextColor(getResources().getColor(R.color.NEGRO));

                contador++;

            } else {

                month2 = selectedMonth;
                day2 = selectedDay;
                year2 = selectedYear;

                if (verificarFechas() == true) {


                    String val1 = "";
                    String val2 = "";

                    if (day2 < 10) {
                        val1 = "0";
                    }

                    if ((month2 + 1) < 10) {
                        val2 = "0";
                    }

                    fecha_final = val1 + day2 + "/" + val2 + (month2 + 1) + "/" + year2;

                    fecha2.setText(fecha_final);
                    fecha2.setTextColor(getResources().getColor(R.color.NEGRO));

                } else {
                    fecha2.setText("DD/MM/AAAA");
                    fecha2.setTextColor(getResources().getColor(R.color.GRIS));
                    Toast.makeText(getActivity(), "La fecha final no es valida, por favor verifiquela e ingresela de nuevo.", Toast.LENGTH_LONG).show();
                }
            }

        }
    };

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
            elem.startWebAccess(rootView, getActivity(), fecha_inicio, fecha_final);
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

