package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.arkamovil.android.procesos.CrearTablas;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WS_Elemento {

    private final String NAMESPACE = "arkaurn:arka";
    private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    //private final String URL = "http://10.20.2.12/arka/index.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/consultar_elementos";
    private final String METHOD_NAME = "consultar_elementos";

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private View rootView;

    private int contador = 0;

    private List<String> descripcion = new ArrayList<String>();
    private List<String> id_elemento = new ArrayList<String>();

    public List<String> getDescripcion() {
        return descripcion;
    }

    public List<String> getId_elemento() {
        return id_elemento;
    }

    public void startWebAccess(View rootView, Activity actividad, int documento_fun) {

        if (contador == 0) {

            this.rootView = rootView;
            this.act = actividad;

            thread = new Thread() {
                public void run() {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("documento_funcionario", 1);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(URL);

                    try {

                        httpTransport.call(SOAP_ACTION, envelope);
                        //SoapObject response = (SoapObject) envelope.getResponse();
                        //Log.v("Aqui", response.getProperty(0).toString());

                        SoapObject obj1 = (SoapObject) envelope.getResponse();
                        for (int i = 0; i < obj1.getPropertyCount(); i++) {
                            SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                            id_elemento.add(obj2.getProperty("id_elemento").toString());
                            descripcion.add(obj2.getProperty("descripcion").toString());
                        }

                    } catch (Exception exception) {
                    }
                    handler.post(createUI);
                }
            };

            thread.start();

        }

    }

    final Runnable createUI = new Runnable() {

        public void run() {
            //Clase para crear Tablas, se envian como parametros la Vista, La Actividad y el numero de Filas.
            CrearTablas crear = new CrearTablas();
            crear.crear(rootView, act, id_elemento, descripcion);
            ////////////////////////////////////////////////////////////////////////////////////////////////
        }
    };

}