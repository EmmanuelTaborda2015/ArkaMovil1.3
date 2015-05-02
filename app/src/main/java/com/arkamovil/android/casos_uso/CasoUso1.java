package com.arkamovil.android.casos_uso;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.herramientas.Despliegue;
import com.arkamovil.android.procesos.GenerarPDF_ActaVisita;
import com.arkamovil.android.servicios_web.WS_Dependencia;
import com.arkamovil.android.servicios_web.WS_NumeroVisitas;
import com.arkamovil.android.servicios_web.WS_RegistroActaVisita;

import java.util.Calendar;


public class CasoUso1 extends Fragment {

    private static int year1;
    private static int month1;
    private static int day1;
    private static int year2;
    private static int month2;
    private static int day2;
    private TextView tvDisplayDate1;
    private TextView tvDisplayDate2;

    private AutoCompleteTextView sede;
    private AutoCompleteTextView facultad;
    private AutoCompleteTextView dependencia;
    private AutoCompleteTextView nombRes;
    private AutoCompleteTextView docRes;
    private EditText observacion;
    private AutoCompleteTextView numVisita;
    private TextView proximaVis;

    private String proxVisita;
    private String fecha;
    private String sede_s;
    private String facultad_s;
    private String dependencia_s;
    private String nombRes_s;
    private String docRes_s;
    private String observacion_s;
    private String numVisita_s;

    private Thread thread_registrarActa;
    private Thread thread_numvisitas;
    private Handler handler = new Handler();
    private String webResponse;

    private View rootView;

    private int contador = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_casouso1, container, false);

        sede = (AutoCompleteTextView) rootView.findViewById(R.id.sede_c1);
        facultad = (AutoCompleteTextView) rootView.findViewById(R.id.facultad_c1);
        dependencia = (AutoCompleteTextView) rootView.findViewById(R.id.dependencia_c1);
        nombRes = (AutoCompleteTextView) rootView.findViewById(R.id.nombreresponsable_c1);
        docRes = (AutoCompleteTextView) rootView.findViewById(R.id.cedularesponsable_c1);
        observacion = (EditText) rootView.findViewById(R.id.observacion_c1);
        numVisita = (AutoCompleteTextView) rootView.findViewById(R.id.numerovisita);
        proximaVis = (TextView) rootView.findViewById(R.id.proxvis_c1);


        thread_numvisitas = new Thread() {
            public void run() {
                WS_NumeroVisitas visitas = new WS_NumeroVisitas();
                webResponse = visitas.startWebAccess();
                handler.post(createUI);
            }
        };

        thread_numvisitas.start();

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


        tvDisplayDate1 = (TextView) rootView.findViewById(R.id.fecha);
        tvDisplayDate2 = (TextView) rootView.findViewById(R.id.proxvis_c1);

        tvDisplayDate1.setTextColor(getResources().getColor(R.color.NEGRO));
        tvDisplayDate1.setText(val1 + day1 + "-" + val2 + (month1 + 1) + "-" + year1);

        contador++;

        tvDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                year2 = c.get(Calendar.YEAR);
                month2 = c.get(Calendar.MONTH);
                day2 = c.get(Calendar.DAY_OF_MONTH);

                if (contador > 0) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                            year1, month1, day1);
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Porfavor ingrese los datos en orden", Toast.LENGTH_LONG).show();
                }
            }
        });

        WS_Dependencia cargar_dependencias = new WS_Dependencia();
        cargar_dependencias.startWebAccess(getActivity(), dependencia);

        Despliegue dep = new Despliegue(dependencia);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        Button descargarPDF = (Button) rootView.findViewById(R.id.descargar);
        descargarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validador = 0;


                if ("".equals(String.valueOf(sede.getText()))) {
                    Toast.makeText(getActivity(), "Porfavor ingrese la Sede", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(facultad.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese la Facultad", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(dependencia.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese la Dependencia", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(nombRes.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese el Nombre del responsable", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(docRes.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese la Cédula del responsable", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(observacion.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese las Observaciones", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("".equals(String.valueOf(numVisita.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese el Número de visita", Toast.LENGTH_LONG).show();
                    validador++;
                } else if ("dd:mm:aa".equals(String.valueOf(proximaVis.getText())) && validador == 0) {
                    Toast.makeText(getActivity(), "Porfavor ingrese la fecha de la próxima visita", Toast.LENGTH_LONG).show();
                    validador++;
                }


                if (validador == 0) {

                    fecha = String.valueOf(day1) + "-" + String.valueOf(month1 + 1) + "-" + String.valueOf(year1);
                    proxVisita = String.valueOf(day2) + "-" + String.valueOf(month2 + 1) + "-" + String.valueOf(year2);
                    sede_s = String.valueOf(sede.getText());
                    facultad_s = String.valueOf(facultad.getText());
                    dependencia_s = String.valueOf(dependencia.getText());
                    nombRes_s = String.valueOf(nombRes.getText());
                    docRes_s = String.valueOf(docRes.getText());
                    observacion_s = String.valueOf(observacion.getText());
                    numVisita_s = String.valueOf(numVisita.getText());

                    thread_registrarActa = new Thread() {
                        public void run() {
                            WS_RegistroActaVisita enviar = new WS_RegistroActaVisita();
                            enviar.startWebAccess(dependencia_s, nombRes_s, docRes_s, observacion_s, fecha, proxVisita);
                        }
                    };

                    thread_registrarActa.start();

                    GenerarPDF_ActaVisita generar = new GenerarPDF_ActaVisita();
                    generar.generar(getResources(), fecha, sede_s, facultad_s, dependencia_s, nombRes_s, docRes_s, observacion_s, numVisita_s, proxVisita);
                }

            }
        });


        return rootView;
    }


    //Función que permite asignar la fecha seleccionada en el calendario al campo

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            month2 = selectedMonth;
            day2 = selectedDay;
            year2 = selectedYear;

            if (verificarFechas() == true) {
                tvDisplayDate2.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(day2).append("-").append(month2).append("-")
                        .append(year2).append(" "));
                tvDisplayDate2.setTextColor(getResources().getColor(R.color.NEGRO));
            } else {
                Toast.makeText(getActivity(), "La fecha de la próxima visita no es valida, por favor verifiquela e ingresela de nuevo", Toast.LENGTH_LONG).show();
            }

        }
    };

    //Se verifica si la fecha de visita es previa a la fecha de la proxima visita

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

    final Runnable createUI = new Runnable() {

        public void run() {
            AutoCompleteTextView numVisita = (AutoCompleteTextView) rootView.findViewById(R.id.numerovisita);
            if("".equals(String.valueOf(webResponse))){
                numVisita.setText("1");
            }else{
                numVisita.setText(String.valueOf(Integer.parseInt(webResponse)+1));
            }
        }
    };

}