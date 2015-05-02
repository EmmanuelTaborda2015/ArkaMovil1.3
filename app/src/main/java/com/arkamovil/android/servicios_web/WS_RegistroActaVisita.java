package com.arkamovil.android.servicios_web;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WS_RegistroActaVisita {

    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/registrarActaVisita";
    private final String METHOD_NAME = "registrarActaVisita";

    private String webResponse = "";


    public String startWebAccess(String dependencia, String nomres, String cedres, String obser, String fecha, String proxVisita) {


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("dependencia", dependencia);
        request.addProperty("nombre_res", nomres);
        request.addProperty("documento_res", cedres);
        request.addProperty("observacion", obser);
        request.addProperty("fecha", fecha);
        request.addProperty("proxima_vis", proxVisita);


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


