package com.arkamovil.android.servicios_web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

public class WS_Elemento {

    private final String NAMESPACE = "arkaurn:arka";
    //private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
    private final String URL = "http://10.20.0.38/ws_arka_android/servicio.php?wsdl";
    private final String SOAP_ACTION = "arkaurn:arka/consultar_elementos";
    private final String METHOD_NAME = "consultar_elementos";

    public Thread getThread() {
        return thread;
    }

    private Thread thread;
    private Handler handler = new Handler();

    private Activity act;
    private View rootView;
    private int caso;

    private int contador = 0;

    private static List<String> descripcion = new ArrayList<String>();
    private static List<String> id_elemento = new ArrayList<String>();
    private static List<String> nivel = new ArrayList<String>();
    private static List<String> marca = new ArrayList<String>();
    private static List<String> placa = new ArrayList<String>();
    private static List<String> serie = new ArrayList<String>();
    private static List<String> valor = new ArrayList<String>();
    private static List<String> subtotal = new ArrayList<String>();
    private static List<String> iva = new ArrayList<String>();
    private static List<String> total = new ArrayList<String>();


    public static List<String> getTotal() {
        return total;
    }

    public static List<String> getNivel() {
        return nivel;
    }

    public static List<String> getMarca() {
        return marca;
    }

    public static List<String> getPlaca() {
        return placa;
    }

    public static List<String> getSerie() {
        return serie;
    }

    public static List<String> getValor() {
        return valor;
    }

    public static List<String> getSubtotal() {
        return subtotal;
    }

    public static List<String> getIva() {
        return iva;
    }

    public static List<String> getDescripcion() {
        return descripcion;
    }

    public static List<String> getId_elemento() {
        return id_elemento;
    }

    public void startWebAccess(View rootView, Activity actividad, final String nombre_fun, int caso) {

        this.rootView = rootView;
        this.act = actividad;
        this.caso = caso;

        descripcion = new ArrayList<String>();
        id_elemento = new ArrayList<String>();
        nivel = new ArrayList<String>();
        marca = new ArrayList<String>();
        placa = new ArrayList<String>();
        serie = new ArrayList<String>();
        valor = new ArrayList<String>();
        subtotal = new ArrayList<String>();
        iva = new ArrayList<String>();
        total = new ArrayList<String>();

        thread = new Thread() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("nom_fun", nombre_fun);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                try {

                    httpTransport.call(SOAP_ACTION, envelope);

                    SoapObject obj1 = (SoapObject) envelope.getResponse();

                    for (int i = 0; i < obj1.getPropertyCount(); i++) {
                        SoapObject obj2 = (SoapObject) obj1.getProperty(i);
                        id_elemento.add(obj2.getProperty("id_elemento").toString());
                        descripcion.add(obj2.getProperty("descripcion").toString());
                        nivel.add(obj2.getProperty("nivel").toString());
                        marca.add(obj2.getProperty("marca").toString());
                        placa.add(obj2.getProperty("placa").toString());
                        serie.add(obj2.getProperty("serie").toString());
                        valor.add(obj2.getProperty("valor").toString());
                        subtotal.add(obj2.getProperty("subtotal_sin_iva").toString());
                        iva.add(obj2.getProperty("total_iva").toString());
                        total.add(obj2.getProperty("total_iva_con").toString());
                    }

                } catch (Exception exception) {
                }
                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            //Clase para crear Tablas, se envian como parametros la Vista, La Actividad y los valores para cada una de las columnas (ArrayList)
            if (caso == 1) {
                TablaConsultarInventario crear = new TablaConsultarInventario();
                crear.crear(rootView, act, id_elemento, descripcion);
                ImageView bajar = (ImageView) rootView.findViewById(R.id.bajar);
                ImageView subir = (ImageView) rootView.findViewById(R.id.subir);
                bajar.setVisibility(View.VISIBLE);
                subir.setVisibility(View.VISIBLE);
            } else if (caso == 2) {
                TablaModificarInventario crear = new TablaModificarInventario();
                crear.crear(rootView, act, id_elemento, descripcion);
                ImageView bajar = (ImageView) rootView.findViewById(R.id.bajar_6);
                ImageView subir = (ImageView) rootView.findViewById(R.id.subir_6);
                bajar.setVisibility(View.VISIBLE);
                subir.setVisibility(View.VISIBLE);
            } else if (caso == 3) {
                TablaConsultarInventariosAsignados crear = new TablaConsultarInventariosAsignados();
                crear.crear(rootView, act, id_elemento, descripcion);
                ImageView bajar = (ImageView) rootView.findViewById(R.id.bajar_c4);
                ImageView subir = (ImageView) rootView.findViewById(R.id.subir_c4);
                Button pdf = (Button) rootView.findViewById(R.id.generarpdf_c4);
                bajar.setVisibility(View.VISIBLE);
                subir.setVisibility(View.VISIBLE);
                pdf.setVisibility(View.VISIBLE);
            }


        }
    };


}