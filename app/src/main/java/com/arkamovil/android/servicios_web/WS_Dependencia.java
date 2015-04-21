package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WS_Dependencia {

    private final String NAMESPACE = "arkaurn:arka";
    private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    //private final String URL = "http://10.20.2.12/arka/index.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/consultar_dependencias";
    private final String METHOD_NAME = "consultar_dependencias";

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private AutoCompleteTextView spin;

    private List<String> dependecia = new ArrayList<String>();


    public void startWebAccess(final Activity act, final AutoCompleteTextView spin) {

        this.act = act;
        this.spin = spin;

        thread = new Thread() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                try {

                    httpTransport.call(SOAP_ACTION, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    for (int i = 0; i < response.getPropertyCount(); i++) {
                        dependecia.add(response.getProperty(i).toString());
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, android.R.layout.simple_spinner_item, dependecia);
            spin.setAdapter(adapter);
        }
    };

    public List<String> getDependecia() {
        return dependecia;
    }
}