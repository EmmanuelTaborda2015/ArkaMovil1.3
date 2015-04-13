package com.arkamovil.android.servicios_web;

    import org.ksoap2.SoapEnvelope;
    import org.ksoap2.serialization.PropertyInfo;
    import org.ksoap2.serialization.SoapObject;
    import org.ksoap2.serialization.SoapPrimitive;
    import org.ksoap2.serialization.SoapSerializationEnvelope;
    import org.ksoap2.transport.HttpTransportSE;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.os.Handler;
    import android.support.v4.app.Fragment;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.arkamovil.android.R;


public class WS_Login extends  Fragment {

        private final String NAMESPACE = "arkaurn:arka";
        private final String URL = "http://10.0.2.2/ws/servicio.php?wsdl";
        //private final String URL = "http://10.20.2.12/arka/index.php?wsdl";
        private final String SOAP_ACTION = "arkarn:arka/login";
        private final String METHOD_NAME = "login";

        private String webResponse = "";
        private Thread thread;
        private Handler handler = new Handler();

        private Context mensaje;


        public void startWebAccess(Context mens, final String usuario, String contrasena) {

            this.mensaje = mens;



            thread = new Thread() {
                public void run() {
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

                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(createUI);
                }
            };

            thread.start();

        }

        final Runnable createUI = new Runnable() {

            public void run() {

                //Toast.makeText(mensaje, webResponse, Toast.LENGTH_LONG).show();

                if("true".equals(webResponse)){
                    Toast.makeText(mensaje, "Conectado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mensaje, "No Conectado", Toast.LENGTH_LONG).show();
                }
            }
        };
}
