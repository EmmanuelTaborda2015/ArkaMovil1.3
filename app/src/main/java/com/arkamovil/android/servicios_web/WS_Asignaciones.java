package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.arkamovil.android.Informacion.Asignaciones;
import com.arkamovil.android.R;
import com.arkamovil.android.procesos.TablaConsultarInventario;
import com.arkamovil.android.procesos.TablaConsultarInventariosAsignados;
import com.arkamovil.android.procesos.TablaModificarInventario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class WS_Asignaciones {


    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/consultar_asignaciones";
    private final String METHOD_NAME = "consultar_asignaciones";


    private Activity act;

    private static List<String> id_elemento = new ArrayList<String>();
    private static List<String> placa = new ArrayList<String>();
    private static List<String> estado = new ArrayList<String>();
    private static List<String> estado_actualizacion = new ArrayList<String>();
    private static List<String> observaciones = new ArrayList<String>();

    public static List<String> getObservaciones() {
        return observaciones;
    }

    public static List<String> getEstado_Actualizacion() {
        return estado_actualizacion;
    }

    public static List<String> getEstado() {
        return estado;
    }

    public static List<String> getPlaca() {
        return placa;
    }

    public static List<String> getId_elemento() {
        return id_elemento;
    }

    public void startWebAccess(Activity actividad, final String id_elem) {

        this.act = actividad;

        id_elemento = new ArrayList<String>();
        placa = new ArrayList<String>();
        estado = new ArrayList<String>();
        estado_actualizacion = new ArrayList<String>();
        observaciones = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("id_elemento", id_elem);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {

            httpTransport.call(SOAP_ACTION, envelope);

            SoapObject obj1 = (SoapObject) envelope.getResponse();

            for (int i = 0; i < obj1.getPropertyCount(); i++) {
                SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                id_elemento.add(obj2.getProperty("id_elemento").toString());
                placa.add(obj2.getProperty("placa").toString());
                estado.add(obj2.getProperty("estado").toString());
                estado_actualizacion.add(obj2.getProperty("estado_actualizacion").toString());
                observaciones.add(obj2.getProperty("observacion").toString());
            }
        } catch (Exception exception) {
        }

    }


}
