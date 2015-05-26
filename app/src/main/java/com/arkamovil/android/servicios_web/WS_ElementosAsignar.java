package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.TablaAsignarInventarios;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WS_ElementosAsignar {

    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/asignar_elementos";
    private final String METHOD_NAME = "asignar_elementos";

    public Thread getThread() {
        return thread;
    }

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private View rootView;

    private static List<String> descripcion = new ArrayList<String>();
    private static List<String> id_elemento = new ArrayList<String>();
    private static List<String> nivel = new ArrayList<String>();
    private static List<String> marca = new ArrayList<String>();
    private static List<String> placa = new ArrayList<String>();
    private static List<String> serie = new ArrayList<String>();

    public static List<String> getNivel() {
        return nivel;
    }

    public static List<String> getMarca() {
        return marca;
    }

    public static List<String> getPlaca() {
        return placa;
    }

    public static List<String> getSerie() {
        return serie;
    }

    public static List<String> getDescripcion() {
        return descripcion;
    }

    public static List<String> getId_elemento() {
        return id_elemento;
    }

    public void startWebAccess(View rootView, Activity actividad, final String fecha_inicial, final String fecha_final) {

        this.rootView = rootView;
        this.act = actividad;

        descripcion = new ArrayList<String>();
        id_elemento = new ArrayList<String>();
        nivel = new ArrayList<String>();
        marca = new ArrayList<String>();
        placa = new ArrayList<String>();
        serie = new ArrayList<String>();

        thread = new Thread() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("fecha_inicio", fecha_inicial);
                request.addProperty("fecha_final", fecha_final);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                try {

                    httpTransport.call(SOAP_ACTION, envelope);

                    SoapObject obj1 = (SoapObject) envelope.getResponse();

                    for (int i = 0; i < obj1.getPropertyCount(); i++) {
                        SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                        id_elemento.add(obj2.getProperty("id_elemento").toString());
                        descripcion.add(obj2.getProperty("descripcion").toString());
                        nivel.add(obj2.getProperty("nivel").toString());
                        marca.add(obj2.getProperty("marca").toString());
                        placa.add(obj2.getProperty("placa").toString());
                        serie.add(obj2.getProperty("serie").toString());
                    }

                } catch (Exception exception) {
                }
                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            TablaAsignarInventarios tablaAsignarInventarios = new TablaAsignarInventarios();
            tablaAsignarInventarios.crear(rootView, act, id_elemento, descripcion, placa);

            if (id_elemento.size() > 0) {
                ImageView bajar = (ImageView) rootView.findViewById(R.id.bajar_c2);
                ImageView subir = (ImageView) rootView.findViewById(R.id.subir_c2);
                Button asignar = (Button) rootView.findViewById(R.id.asignar_c2);

                bajar.setVisibility(View.VISIBLE);
                subir.setVisibility(View.VISIBLE);
                asignar.setVisibility(View.VISIBLE);
            }

            Button consultar = (Button) rootView.findViewById(R.id.con_c2);
            consultar.setEnabled(true);

        }
    };


}