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
    private final String SOAP_ACTION = "urn:arka/elementosFuncionario";
    private final String METHOD_NAME = "elementosFuncionario";

    private List<String> id_elemento = new ArrayList<String>();
    private List<String> placa = new ArrayList<String>();
    private List<String> descripcion = new ArrayList<String>();
    private List<String> estado = new ArrayList<String>();
    private List<String> marca = new ArrayList<String>();
    private List<String> serie = new ArrayList<String>();
    private List<String> nivel = new ArrayList<String>();
    private List<String> total = new ArrayList<String>();
    private List<String> tipo_bien = new ArrayList<String>();
    private List<String> fecha_asig = new ArrayList<String>();
    private List<String> ubicacion_esp = new ArrayList<String>();
    private List<String> id_levantamiento = new ArrayList<String>();

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
                    placa.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    placa.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(5);
                    descripcion.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    descripcion.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(7);
                    estado.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    estado.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(9);
                    marca.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    marca.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(11);
                    serie.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    serie.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(13);
                    nivel.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    nivel.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(15);
                    total.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    total.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(17);
                    tipo_bien.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    tipo_bien.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(19);
                    fecha_asig.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    fecha_asig.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(21);
                    ubicacion_esp.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    ubicacion_esp.add("");
                }
                try {
                    obj3 = (SoapObject) obj2.getProperty(23);
                    id_levantamiento.add(obj3.getProperty("value").toString());
                } catch (NullPointerException ex) {
                    id_levantamiento.add("");
                }
            }
        } catch (Exception exception) {
            Log.v("mensaje", exception.toString());
        }
    }
}