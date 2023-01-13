package com.example.proyectofinal.util;

import java.util.ArrayList;
import java.util.List;

public class Imagenes {

    private static final Imagenes imagenes = new Imagenes();

    public static Imagenes getInstance(){
        return imagenes;
    }

    private final List<Imagen> imagenesList;

    private Imagenes(){
        this.imagenesList = new ArrayList<>();
    };

    public List<Imagen> getList(){
        return this.imagenesList;
    }

    public void addImagen(Imagen imagen){
        this.imagenesList.add(imagen);
    }


}
