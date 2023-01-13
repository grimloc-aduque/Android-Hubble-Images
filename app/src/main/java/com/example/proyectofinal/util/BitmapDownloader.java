package com.example.proyectofinal.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapDownloader {
    public static Bitmap download(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            connection.disconnect();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
