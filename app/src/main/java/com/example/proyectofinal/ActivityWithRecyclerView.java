package com.example.proyectofinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.util.Imagen;

public abstract class ActivityWithRecyclerView extends AppCompatActivity {

    protected RecyclerView imagenesRecyclerView;
    protected ImagenesAdapter imagenesAdapter;

    public void refreshRecyclerView(){
        this.imagenesAdapter.notifyDataSetChanged();
    }

    public abstract void doAfterImagenClicked(Imagen imagen);

    public abstract void doAfterImagenBorrada(Imagen imagen);

    // Permisos para compartir imagen

    public boolean hasWritingPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestWritingPermissions(){
        this.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted. Click on share again.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}
