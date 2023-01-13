package com.example.proyectofinal.util;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    public static Map<String, Bitmap> iconos = new HashMap<>();

    public static Map<String, Bitmap> imagenes = new HashMap<>();

    public static void liberarCache(Imagen imagen){
        iconos.remove(imagen.getURLIcono());
        imagenes.remove(imagen.getURLImagen());
    }

}
