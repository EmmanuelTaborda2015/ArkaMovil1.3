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

public class WS_Sede {

    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/consultar_sede_oracle";
    private final String METHOD_NAME = "consultar_sede_oracle";

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private AutoCompleteTextView spin;

    private List<String> sede = new ArrayList<String>();
    private List<String> id_sede = new ArrayList<String>();

    public List<String> getId_sede() {
        return id_sede;
    }

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

                    SoapObject obj1 = (SoapObject) envelope.getResponse();

                    for (int i = 0; i < obj1.getPropertyCount(); i++) {
                        SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                        id_sede.add(obj2.getProperty("id").toString());
                        sede.add(obj2.getProperty("nombre").toString());
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, android.R.layout.simple_spinner_item, sede);
            spin.setAdapter(adapter);
        }
    };

    public List<String> getSede() {
        return sede;
    }

}