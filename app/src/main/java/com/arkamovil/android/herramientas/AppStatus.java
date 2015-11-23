package com.arkamovil.android.herramientas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class AppStatus {

    private static AppStatus instance = new AppStatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static AppStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}

/*AppStatus status = new AppStatus();
if (status.getInstance(this).isOnline()) {

        Toast.makeText(getApplicationContext(),"You are online!!!!", Toast.LENGTH_LONG).show();

        } else {

        Toast.makeText(getApplicationContext(),"You are not online!!!!", Toast.LENGTH_LONG).show();
        Log.v("Home", "############################You are not online!!!!");
        }*/

/*
private String METHOD_NAME;
private String NAMESPACE;
private String SOAP_ACTION;
private String URL;
private int TimeOut=3000;//
private SoapObject so;
SoapSerializationEnvelope envelope;
HttpTransportSE androidHttpTransport;
try
        {

        METHOD_NAME = "myutility";
        NAMESPACE = "http://";
        SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread.sleep(2000);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        URL = "http://www.example.com";
        androidHttpTransport = new HttpTransportSE(URL,Time_Out);
        androidHttpTransport.call(SOAP_ACTION,envelope);
        so = (SoapObject)envelope.bodyIn;
        String s=so.toString();
        //Your processing here
        }
        catch(InterruptedException e)
        {
        //When timeout occurs handles this....

        }
        catch( Exception e )
        {}*/
