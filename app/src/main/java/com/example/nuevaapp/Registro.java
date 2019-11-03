package com.example.nuevaapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nuevaapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Registro extends AppCompatActivity {
    Button btnConsultar, btnGuardar;
    EditText etDni, etNombres, etApellidos;
    String urlConsult, urlCargar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConsultar = (Button)findViewById(R.id.btnConsultar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        etDni = (EditText)findViewById(R.id.etDni);
        etNombres = (EditText)findViewById(R.id.etNombres);
        etApellidos = (EditText)findViewById(R.id.etApellidos);
        btnConsultar.setOnClickListener((View.OnClickListener) new ConsultarDatos().execute(urlConsult+etDni.getText().toString()));
        btnGuardar.setOnClickListener((View.OnClickListener) new CargarDatos().execute("https://www.google.com/api"+etDni.getText().toString()+"&nombres="+etNombres.getText().toString()+"&apellidos="+etApellidos.getText().toString()));


    }
    public class ConsultarDatos extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch (Exception e){
                return "Error " + e;
            }
        }
    }
    public class CargarDatos extends AsyncTask<String, Void, String> {
         @Override
        protected String doInBackground(String... urls){
            try {
                return downloadUrl(urls[0]);
            }catch (Exception e){
                return  "Error: "+e;
            }
        }
        @Override
        protected void onPostExecute(String result){
            JSONArray ja = null;
            try{
                ja = new JSONArray(result);
                etDni.setText(ja.getString(1));
                etNombres.setText(ja.getString(2));
                etApellidos.setText(ja.getString(3));
            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
    private String downloadUrl(String myurl) throws IOException{
        Log.i("URL", ""+myurl);
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 500;

        try{
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "La respuesta es:" + response);
            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            return contentAsString;

        } finally {
            if (is != null){
                is.close();
            }
        }




    }

    public String readIt(InputStream stream, int len) throws  IOException, UnsupportedEncodingException{
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }




}
