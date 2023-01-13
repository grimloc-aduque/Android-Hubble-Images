package com.example.proyectofinal;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.example.proyectofinal.util.Cache;
import com.example.proyectofinal.util.Historico;
import com.example.proyectofinal.util.Imagen;
import com.example.proyectofinal.util.Imagenes;
import com.google.gson.Gson;

public class MainActivity extends ActivityWithRecyclerView {

    private Imagenes imagenes;
    private Historico historico;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imagenes = Imagenes.getInstance();
        this.historico = Historico.getInstance();
        this.sharedPreferences = getSharedPreferences(Historico.sharedPreferencesFile, MODE_PRIVATE);
        this.historico.loadData(this.sharedPreferences);

        this.imagenesRecyclerView = findViewById(R.id.recyclerViewImagenes);
        this.imagenesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.imagenesAdapter = new ImagenesAdapter(this, this.imagenes.getList());
        this.imagenesRecyclerView.setAdapter(this.imagenesAdapter);

        new GetImagenesTask().execute();
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.historico.saveData(this.sharedPreferences);
    }

    public void onHistorico(View v){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }

    // ActivityWithRecyclerView implementations

    @Override
    public void doAfterImagenClicked(Imagen imagen){
        this.historico.addImagen(imagen);
    }

    @Override
    public void doAfterImagenBorrada(Imagen imagen){
        this.refreshRecyclerView();
        Cache.liberarCache(imagen);
    }


    // Background Task para obtener la lista de imágenes

    private class GetImagenesTask extends AsyncTask<Void, Void, String> {

        // Descarga el HTML y extrae el arreglo JSON

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://esahubble.org/images/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return extractJson(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String extractJson(HttpURLConnection connection) throws IOException {
            StringBuilder htmlBuilder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line);
            }
            String html = htmlBuilder.toString();
            String json = html.split("var images = \\[")[1].split("]")[0];
            return String.format("[%s]", json);
        }


        // Transforma el JSON en una lista de imagenes

        @Override
        protected void onPostExecute(String json) {
            if(json != null){
                Gson gson = new Gson();
                Imagen[] objetosImagen = gson.fromJson(json, Imagen[].class);
                Arrays.stream(objetosImagen).forEach(
                    imagen -> {
                        if(imagen != null)
                            imagenes.addImagen(imagen);
                    }
                );
                refreshRecyclerView();
            }
        }
    }

}