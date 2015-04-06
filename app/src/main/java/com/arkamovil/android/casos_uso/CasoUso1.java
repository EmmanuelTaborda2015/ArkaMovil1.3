package com.arkamovil.android.casos_uso;

        import android.app.DatePickerDialog;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.arkamovil.android.R;
        import com.arkamovil.android.procesos.LlenarListas;

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

    private int caso = 0;
    private int contador = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_casouso1, container, false);

        TextView fecha = (TextView) rootView.findViewById(R.id.fecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvDisplayDate1 = (TextView) getView().findViewById(R.id.fecha);
                tvDisplayDate2 = (TextView) getView().findViewById(R.id.proxvis);

                final Calendar c = Calendar.getInstance();
                year1 = c.get(Calendar.YEAR);
                month1 = c.get(Calendar.MONTH);
                day1 = c.get(Calendar.DAY_OF_MONTH);
                caso = 1;

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                        year1, month1, day1);
                dialog.show();
            }
        });

        TextView proxvis = (TextView) rootView.findViewById(R.id.proxvis);
        proxvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Calendar c = Calendar.getInstance();
                year2 = c.get(Calendar.YEAR);
                month2 = c.get(Calendar.MONTH);
                day2 = c.get(Calendar.DAY_OF_MONTH);
                caso = 2;

                if (contador > 0) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                            year1, month1, day1);
                    dialog.show();
                }else{
                    Toast.makeText(getActivity(), "Porfavor ingrese los datos en orden", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Se crea un objeto de la clse Llenar Spinner y se llama la función llenar, que es la encargada de cargar los datos al Spinner.
        //Se envian como parametros la actividad y el Spinner a llenar. posiblemente tambien se envie una consulta.

        LlenarListas llenar = new LlenarListas();
        llenar.llenarSpinner(getActivity(),(Spinner) rootView.findViewById(R.id.sede));

        //////////////////////////////////////////////////////////////////////////////////////////////////

        return rootView;
    }


    //Función que permite asignar la fecha seleccionada en el calendario al campo

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            if(caso==1){
                month1 = selectedMonth;
                day1 = selectedDay;
                year1 = selectedYear;
                tvDisplayDate1.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(day1).append("-").append(month1+1).append("-")
                        .append(year1).append(" "));
                        tvDisplayDate1.setTextColor(getResources().getColor(R.color.NEGRO));
                if( !tvDisplayDate2.getText().equals("")){
                    tvDisplayDate2.setTextColor(getResources().getColor(R.color.GRIS));
                    tvDisplayDate2.setText("dd:mm:aa");

                }
                contador++;
            }

            if(caso==2){
                month2 = selectedMonth;
                day2 = selectedDay;
                year2 = selectedYear;

                if(verificarFechas()==true){
                    tvDisplayDate2.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(day2).append("-").append(month2).append("-")
                            .append(year2).append(" "));
                            tvDisplayDate2.setTextColor(getResources().getColor(R.color.NEGRO));
                }else{
                    Toast.makeText(getActivity(), "La fecha de la próxima visita no es valida, por favor verifiquela e ingresela de nuevo", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    //Se verifica si la fecha de visita es previa a la fecha de la proxima visita

    public boolean verificarFechas(){

        boolean verificar = false;

        if(year1 <= year2){
            if(month1 <= month2){
                if(day1<=day2){
                    verificar = true;
                }
            }
        }
        return verificar;
    }

}