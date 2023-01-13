package com.example.proyectofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.util.BitmapDownloader;
import com.example.proyectofinal.util.Cache;

import java.util.HashMap;
import java.util.Map;

public class DetalleActivity extends AppCompatActivity {


    private static LoadImageTask taskRunning;

    TextView textViewHeader;
    ImageView imageView;
    String urlImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_detalle);

        this.textViewHeader = findViewById(R.id.textViewHeader);
        this.imageView = findViewById(R.id.imageView);

        Intent intentRecibido = this.getIntent();
        this.textViewHeader.setText(intentRecibido.getStringExtra("titulo"));
        this.urlImagen = intentRecibido.getStringExtra("url");

        if (Cache.imagenes.containsKey(this.urlImagen)) {
            this.imageView.setImageBitmap(Cache.imagenes.get(urlImagen));
        }
        else {
            LoadImageTask loadImageTask = new LoadImageTask();
            DetalleActivity.taskRunning = loadImageTask;
            loadImageTask.execute();
        }
    }

    public void onRegresar(View v){
        finish();
    }


    // Background Task para descargar imagen

    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute(){
            Log.d("Request Image: ", urlImagen);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmapImagen = BitmapDownloader.download(urlImagen);
            if(bitmapImagen != null)
                Cache.imagenes.put(urlImagen, bitmapImagen);
            return bitmapImagen;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapImagen) {
            Log.d("Response Image: ", urlImagen);
            if(this == taskRunning && bitmapImagen!=null )
                imageView.setImageBitmap(bitmapImagen);
        }
    }
}
