package com.arkamovil.android.servicios_web;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WS_Sede {

    private final String NAMESPACE = "arkaurn:arka";
    private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    //private final String URL = "http://10.20.2.12/arka/index.php?wsdl";
    private final String SOAP_ACTION = "arkarn:arka/login";
    private final String METHOD_NAME = "login";

    private String webResponse = "";

    public String startWebAccess(final String usuario, String contrasena) {

        try {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            PropertyInfo fromProp = new PropertyInfo();

            request.addProperty("usuario", usuario);
            request.addProperty("contrasenna", "eab41e38426312cf48baaaf80af9ee88b6023a44");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    URL);

            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope
                    .getResponse();
            webResponse = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return webResponse;

    }
}
