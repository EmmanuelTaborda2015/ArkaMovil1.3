package com.arkamovil.android.servicios_web;

import android.text.format.Time;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WS_EnviarElementosAsignar {

    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/asignar_elementos_funcionario";
    private final String METHOD_NAME = "asignar_elementos_funcionario";

    private String webResponse = "";


    public String startWebAccess(String sede, String dependencia, String funcionario, String id_elemento) {


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("sede", sede);
        request.addProperty("dependencia", dependencia);
        request.addProperty("funcionario", funcionario);
        request.addProperty("id_elemento", id_elemento);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        request.addProperty("fecha_registro",today.monthDay + "/" + (today.month + 1) + "/" + today.year);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);


        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            webResponse = response.toString();
        } catch (Exception exception) {
        }

        return webResponse;
    }

}