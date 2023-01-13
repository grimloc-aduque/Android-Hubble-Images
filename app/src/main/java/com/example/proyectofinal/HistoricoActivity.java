package com.example.proyectofinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectofinal.util.Cache;
import com.example.proyectofinal.util.Historico;
import com.example.proyectofinal.util.Imagen;

public class HistoricoActivity extends ActivityWithRecyclerView {
    Historico historico;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        this.historico = Historico.getInstance();
        this.sharedPreferences = getSharedPreferences(Historico.sharedPreferencesFile, MODE_PRIVATE);

        this.imagenesRecyclerView = findViewById(R.id.recyclerViewImagenes);
        this.imagenesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.imagenesAdapter = new ImagenesAdapter(this, this.historico.getImagenesList());
        this.imagenesRecyclerView.setAdapter(this.imagenesAdapter);
    }

    public void onRegresar(View v){
        finish();
    }

    public void onBorrarHistorico(View v){
        this.historico.getImagenesList().forEach(Cache::liberarCache);
        this.historico.removeAll();
        this.refreshRecyclerView();
        this.historico.saveData(this.sharedPreferences);
    }


    // ActivityWithRecyclerView implementations

    @Override
    public void doAfterImagenClicked(Imagen imagen){}

    @Override
    public void doAfterImagenBorrada(Imagen imagen){
        Cache.liberarCache(imagen);
        this.refreshRecyclerView();
        this.historico.saveData(this.sharedPreferences);
    }
}
