package com.arkamovil.android.servicios_web;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WS_ElementosInventario {

    private final String NAMESPACE = "urn:arka";
    private final String URL = "http://10.20.0.38/WS_ARKA/servicio/servicio.php";
    private final String SOAP_ACTION = "urn:arka/elementosInventario";
    private final String METHOD_NAME = "elementosInventario";

    private List<String> doc_fun = new ArrayList<String>();
    private List<String> nomb_fun = new ArrayList<String>();

    private List<String> id_sede = new ArrayList<String>();
    private List<String> sede = new ArrayList<String>();

    private List<String> id_dependencia = new ArrayList<String>();
    private List<String> dependencia = new ArrayList<String>();

    private List<String> id_espacio = new ArrayList<String>();
    private List<String> espacio = new ArrayList<String>();

    private List<String> id_elemento = new ArrayList<String>();

    public List<String> getId_elemento() {
        return espacio;
    }

    public List<String> getEspacio() {
        return espacio;
    }

    public List<String> getId_espacio() {
        return id_espacio;
    }

    public List<String> getDependencia() {
        return dependencia;
    }

    public List<String> getId_dependencia() {
        return id_dependencia;
    }

    public List<String> getSede() {
        return sede;
    }

    public List<String> getId_sede() {
        return id_sede;
    }

    public List<String> getNomb_fun() {
        return nomb_fun;
    }

    public List<String> getDoc_fun() {
        return doc_fun;
    }

    public void startWebAccess(final String doc_funcioanario, final String id_dep) {

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("funcionario", doc_funcioanario);
        request.addProperty("dependencia", id_dep);

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
                    id_elemento.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    id_elemento.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(3);
                    doc_fun.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    doc_fun.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(5);
                    nomb_fun.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    nomb_fun.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(7);
                    id_sede.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    id_sede.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(9);
                    sede.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    sede.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(11);
                    id_dependencia.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    id_dependencia.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(13);
                    dependencia.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    dependencia.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(15);
                    id_espacio.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    espacio.add("");
                }
            }
        } catch (Exception exception) {
            Log.v("mensaje", exception.toString());
        }
    }
}