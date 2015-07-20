package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.TablaInventarios;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WS_InventarioTipoConfirmacion {

    private final String NAMESPACE = "urn:arka";
    private final String URL = "http://10.20.0.38/WS_ARKA/servicio/servicio.php";
    private final String SOAP_ACTION = "urn:arka/tipoConfirmacionInventario";
    private final String METHOD_NAME = "tipoConfirmacionInventario";

    private Thread thread;
    private Handler handler = new Handler();

    private Activity actividad;
    private View vista;

    private List<String> nomb_fun = new ArrayList<String>();
    private List<String> doc_fun = new ArrayList<String>();
    private List<String> sede = new ArrayList<String>();
    private List<String> dependencia = new ArrayList<String>();
    private List<String> ubicacion = new ArrayList<String>();

    public List<String> getNomb_fun() {
        return nomb_fun;
    }

    public List<String> getDoc_fun() {
        return doc_fun;
    }

    public List<String> getSede() {
        return sede;
    }

    public List<String> getDependencia() {
        return dependencia;
    }

    public List<String> getUbicacion() {
        return ubicacion;
    }

    public void startWebAccess(final View rootview, final Activity act) {

        this.vista = rootview;
        this.actividad = act;

        thread = new Thread() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                try {

                    httpTransport.call(SOAP_ACTION, envelope);
                    SoapObject obj1 = (SoapObject)envelope.bodyIn;

                    Vector<?> responseVector = (Vector<?>) obj1.getProperty(0);

                    for (int i = 0; i < responseVector.size(); i++) {
                        SoapObject obj2 = (SoapObject) responseVector.get(i);
                        SoapObject obj3;
                        try{
                            obj3 = (SoapObject) obj2.getProperty(1);
                            doc_fun.add(obj3.getProperty("value").toString());
                        }catch (NullPointerException ex){
                            doc_fun.add("");
                        }
                        try{
                            obj3 = (SoapObject) obj2.getProperty(3);
                            sede.add(obj3.getProperty("value").toString());
                        }catch (NullPointerException ex){
                            sede.add("");
                        }
                        try{
                            obj3 = (SoapObject) obj2.getProperty(5);
                            dependencia.add(obj3.getProperty("value").toString());
                        }catch (NullPointerException ex){
                            dependencia.add("");
                        }
                        try{
                            obj3 = (SoapObject) obj2.getProperty(7);
                            ubicacion.add(obj3.getProperty("value").toString());
                        }catch (NullPointerException ex){
                            ubicacion.add("");
                        }
                    }

                } catch (Exception exception) {
                    Log.v("mensaje", exception.toString());
                }
                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            TablaInventarios tablaInventarios = new TablaInventarios();
            tablaInventarios.crear(vista, actividad, doc_fun, sede, dependencia, ubicacion);

//            if (doc_fun.size() > 0) {
//                ImageView bajar = (ImageView) vista.findViewById(R.id.bajar_c2);
//                ImageView subir = (ImageView) vista.findViewById(R.id.subir_c2);
//
//                bajar.setVisibility(View.VISIBLE);
//                subir.setVisibility(View.VISIBLE);
//            }
        }
    };
}