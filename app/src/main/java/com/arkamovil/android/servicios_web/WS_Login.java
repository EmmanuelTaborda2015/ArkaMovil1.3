package com.arkamovil.android.servicios_web;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class WS_Login {
    private final String NAMESPACE = "urn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    //private final String URL = "http://10.20.0.38/WS_ARKA/servicio/servicio.php";
    private String URL;
    private final String SOAP_ACTION = "urn:arka/login";
    private final String METHOD_NAME = "login";
    private String webResponse = "";
    public String startWebAccess(String usuario, String contrasena, String id_dispositivo) {

        Datos url = new Datos();
        URL = url.getURL();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("usuario", usuario);
        request.addProperty("contrasenna", contrasena);
        request.addProperty("dispositivo", id_dispositivo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();
            String statuses = envelope.getResponse().toString();
            webResponse = response.toString();
            Log.v("mensaje", webResponse);

        } catch (Exception exception) {
            Log.v("emma","no conection|" );
        }
        return webResponse;
    }
}