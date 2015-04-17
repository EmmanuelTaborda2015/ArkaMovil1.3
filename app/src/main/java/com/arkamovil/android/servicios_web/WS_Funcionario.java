package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WS_Funcionario{

    private final String NAMESPACE = "arkaurn:arka";
    private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    //private final String URL = "http://10.20.2.12/arka/index.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/gethelloworld";
    private final String METHOD_NAME = "gethelloworld";

    private String webResponse = "";


    public void startWebAccess(Activity act,AutoCompleteTextView spin) {


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("name", "Ema");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {

            httpTransport.call(SOAP_ACTION, envelope);

            List<String> toSpin = new ArrayList<String>();
            toSpin.add("sede 40");
            toSpin.add("Sede Vivero");
            toSpin.add("Sede Macarena");
            toSpin.add("Sede Tecnologica");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_spinner_item,toSpin);
            spin.setAdapter(adapter);

            Object response = envelope.getResponse();
            webResponse = response.toString();


        } catch (Exception exception) {
        }
    }

}