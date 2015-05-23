package com.arkamovil.android.casos_uso;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arkamovil.android.R;
import com.arkamovil.android.procesos.GenerarPDF_Inventarios;
import com.arkamovil.android.servicios_web.WS_CargarImagen;
import com.arkamovil.android.servicios_web.WS_ConsultarPlacaImagen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Emmanuel on 5/04/15.
 */
public class AsociarImagen extends Fragment {

    private View rootView;
    private String foto;
    private int opcion = 0;
    private final static String NOMBRE_DIRECTORIO = "ImagenesElementos";
    private Button btnCamara;
    private Button scanear;
    private Button asignar;
    private static String imagen;
    private static String id_elemento;
    private static String id;

    private Thread thread;
    private Thread thread_placa;
    private Handler handler = new Handler();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fm_cargarimagen, container, false);

        scanear = (Button) rootView.findViewById(R.id.escanear_c3);
        btnCamara = (Button) rootView.findViewById(R.id.camara);
        asignar = (Button) rootView.findViewById(R.id.asignarimagen);

        //btnCamara.setEnabled(false);
        //asignar.setEnabled(false);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri output = Uri.fromFile(new File(foto));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, 100);
                opcion = 1;
            }
        });

        asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanear.setEnabled(false);
                btnCamara.setEnabled(false);
                asignar.setEnabled(false);

                thread = new Thread() {
                    public void run() {

                        WS_CargarImagen ws_cargarImagen = new WS_CargarImagen();
                        ws_cargarImagen.startWebAccess(id_elemento, imagen);

                        handler.post(createUI);
                    }
                };

                thread.start();

            }
        });

        scanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnCamara.setEnabled(false);
                asignar.setEnabled(false);

                Intent intent = new Intent("com.arkamovil.android.SCAN");
                startActivityForResult(intent, 0);
                opcion = 2;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (opcion == 1) {
            ImageView iv = (ImageView) rootView.findViewById(R.id.imageView1);
            iv.setImageBitmap(BitmapFactory.decodeFile(foto));

            File file = new File(foto);
            if (file.exists()) {
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(foto);
                    //You can get an inputStream using any IO API
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }

                    bytes = output.toByteArray();
                    imagen = Base64.encodeToString(bytes, Base64.DEFAULT);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                asignar.setEnabled(true);

            } else {
                Toast.makeText(getActivity(), "No se ha realizado la foto", Toast.LENGTH_SHORT).show();
            }
        }
        if (opcion == 2) {
            if (resultCode == getActivity().RESULT_OK) {
                final String contenido = intent.getStringExtra("SCAN_RESULT");
                String formato = intent.getStringExtra("SCAN_RESULT_FORMAT");
                scanear.setEnabled(false);
                thread_placa = new Thread() {
                    public void run() {

                        WS_ConsultarPlacaImagen placa = new WS_ConsultarPlacaImagen();
                        id = placa.startWebAccess(contenido);

                        handler.post(Elemento);
                    }
                };

                thread_placa.start();

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Se cancelo el escaneo de la placa", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static File getRuta() {

        // El fichero ser� almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

    final Runnable createUI = new Runnable() {

        public void run() {
            Toast.makeText(getActivity(), "Ha sido cargado la imagen al elemento", Toast.LENGTH_SHORT).show();
            scanear.setEnabled(true);
            btnCamara.setEnabled(true);
            asignar.setEnabled(true);
        }
    };

    final Runnable Elemento = new Runnable() {

        public void run() {
            Log.v("id", id);
            if ("false".equals(id)) {
                Toast.makeText(getActivity(), "No se encontro ningun elemento con la placa escaneada", Toast.LENGTH_LONG).show();
            } else {
                id_elemento = id;
                foto = Environment.getExternalStorageDirectory() + "/imagen" + id_elemento + ".jpg";
                btnCamara.setEnabled(true);
                asignar.setEnabled(false);
            }
            scanear.setEnabled(true);
        }
    };
}