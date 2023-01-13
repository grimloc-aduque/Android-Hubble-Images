package com.example.proyectofinal.util;

public class Imagen {
    private String title;
    private String id;

    public Imagen(String titulo, String id){
        this.title = titulo;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId(){
        return this.id;
    }

    public String getURLImagen(){
        return String.format("https://cdn.spacetelescope.org/archives/images/large/%s.jpg", this.id);
    }

    public String getURLIcono() {
        return String.format("https://cdn.spacetelescope.org/archives/images/thumb300y/%s.jpg", this.id);
    }

    @Override
    public String toString() {
        return "Imagen{" +
                "titulo='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
