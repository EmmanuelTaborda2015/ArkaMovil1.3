package com.arkamovil.android.casos_uso;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.GenerarPDF_ActaVisita;
import com.arkamovil.android.procesos.LlenarListas;
import com.arkamovil.android.procesos.TablaConsultarInventario;

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

    private int contador = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_casouso1, container, false);

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
        tvDisplayDate2 = (TextView) rootView.findViewById(R.id.proxvis);

        tvDisplayDate1.setTextColor(getResources().getColor(R.color.NEGRO));
        tvDisplayDate1.setText(val1 + day1 + ":" + val2 + (month1 + 1) + ":" + year1);

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

        //Se crea un objeto de la clse Llenar Spinner y se llama la función llenar, que es la encargada de cargar los datos al Spinner.
        //Se envian como parametros la actividad y el Spinner a llenar. posiblemente tambien se envie una consulta.

        LlenarListas llenar = new LlenarListas();
        llenar.llenarSpinner(getActivity(), (Spinner) rootView.findViewById(R.id.sede));

        //////////////////////////////////////////////////////////////////////////////////////////////////
        Button descargarPDF = (Button) rootView.findViewById(R.id.descargar);
        descargarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerarPDF_ActaVisita generar = new GenerarPDF_ActaVisita();
                generar.generar(getResources());
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

}