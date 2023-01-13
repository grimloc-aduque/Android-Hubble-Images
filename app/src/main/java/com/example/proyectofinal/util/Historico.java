package com.example.proyectofinal.util;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Historico {
    private static final Historico historico = new Historico();

    public static Historico getInstance(){
        return Historico.historico;
    }

    private final List<Imagen> imagenesList;

    private Historico(){
        this.imagenesList = new ArrayList<>();
    }

    public List<Imagen> getImagenesList(){
        return this.imagenesList;
    }

    public void addImagen(Imagen imagen){
        Imagen currentImg = this.getImagenById(imagen.getId());
        if(currentImg != null)
            this.imagenesList.remove(currentImg);
        this.imagenesList.add(0, imagen);
    }

    private Imagen getImagenById(String imagenId){
        return this.imagenesList.stream()
                .filter(imagen -> imagen.getId().equals(imagenId))
                .findFirst()
                .orElse(null);
    }

    public void removeAll(){
        this.imagenesList.clear();
    }

    // Persistencia

    public static String sharedPreferencesFile = "Historico";
    public static String sharedPreferencesKey = "imagenes";

    public void loadData(SharedPreferences sharedPreferences){
        String json = sharedPreferences.getString(sharedPreferencesKey, "");
        Gson gson = new Gson();
        Imagen[] savedObjects = gson.fromJson(json, Imagen[].class);
        if(savedObjects!=null){
            this.imagenesList.addAll(Arrays.asList(savedObjects));
        }
    }

    public void saveData(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.imagenesList);
        editor.putString(sharedPreferencesKey, json);
        editor.apply();
    }

}
