package com.arkamovil.android.servicios_web;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WS_Observaciones {

    private final String NAMESPACE = "urn:arka";
    private final String URL = "http://10.20.0.38/WS_ARKA/servicio/servicio.php";
    private final String SOAP_ACTION = "urn:arka/consultar_observacion";
    private final String METHOD_NAME = "consultar_observacion";


    private List<String> observacion_funcionario = new ArrayList<String>();
    private List<String> observacion_almacen = new ArrayList<String>();
    private List<String> tipo_movimiento = new ArrayList<String>();

    public List<String> getTipo_movimiento() {
        return tipo_movimiento;
    }

    public List<String> getObservacion_almacen() {
        return observacion_almacen;
    }

    public List<String> getObservacion_funcionario() {
        return observacion_funcionario;
    }

    public void startWebAccess(String id_levantamiento) {


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("id_levantamiento", id_levantamiento);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            Vector<?> responseVector = (Vector<?>) obj1.getProperty(0);

            for (int i = 0; i < responseVector.size(); i++) {
                SoapObject obj2 = (SoapObject) responseVector.get(i);
                SoapObject obj3;
                try {
                    obj3 = (SoapObject) obj2.getProperty(1);
                    observacion_funcionario.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    observacion_funcionario.add("Sin observaciones");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(3);
                    observacion_almacen.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    observacion_almacen.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(5);
                    tipo_movimiento.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    tipo_movimiento.add("");
                }
            }
        } catch (Exception exception) {
            Log.v("mensaje", exception.toString());
        }
    }

}