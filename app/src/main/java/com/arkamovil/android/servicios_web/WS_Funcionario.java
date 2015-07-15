package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.arkamovil.android.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WS_Funcionario {

    private final String NAMESPACE = "urn:arka";
    private final String URL = "http://10.20.0.38/WS_ARKA/servicio/servicio.php";
    private final String SOAP_ACTION = "urn:arka/funcionario";
    private final String METHOD_NAME = "funcionario";

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private AutoCompleteTextView spin;

    private List<String> fun_identificacion = new ArrayList<String>();
    private List<String> fun_nombre = new ArrayList<String>();

    public List<String> getFun_nombre() {
        return fun_nombre;
    }

    public List<String> getFun_identificacion() {
        return fun_identificacion;
    }

    public void startWebAccess(final Activity act, final AutoCompleteTextView spin, final String dependencia) {

        this.act = act;
        this.spin = spin;

        thread = new Thread() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                //request.addProperty("id_objeto", dependencia);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                try {

                    httpTransport.call(SOAP_ACTION, envelope);
                    SoapObject obj1 = (SoapObject)envelope.bodyIn;

                    Vector<?> responseVector = (Vector<?>) obj1.getProperty(0);

                    for (int i = 0; i < responseVector.size(); i++) {
                        SoapObject obj2 = (SoapObject) responseVector.get(i);
                        SoapObject obj3 = (SoapObject) obj2.getProperty(1);
                        fun_nombre.add(obj3.getProperty("value").toString());
                        obj3 = (SoapObject) obj2.getProperty(3);
                        fun_identificacion.add(obj3.getProperty("value").toString());
                        //Log.v("mensaje", "id :" + fun_identificacion.get(i));
                        //Log.v("mensaje", "nombre :" + fun_nombre.get(i));
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, android.R.layout.simple_spinner_item, fun_identificacion);
            spin.setAdapter(adapter);

            if(fun_identificacion.size() == 0){
                Toast.makeText(act, "VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
            }
        }
    };

}