package com.example.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.util.BitmapDownloader;
import com.example.proyectofinal.util.Cache;
import com.example.proyectofinal.util.Imagen;

import java.io.OutputStream;
import java.util.List;

public class ImagenesAdapter extends RecyclerView.Adapter<ImagenesAdapter.ViewHolder>{

    List<Imagen> imagenesList;
    ActivityWithRecyclerView context;

    public ImagenesAdapter(ActivityWithRecyclerView context, List<Imagen> imagenesList){
        this.imagenesList = imagenesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d("BIND POSITION: ", String.valueOf(position));
        Imagen imagen = imagenesList.get(position);
        viewHolder.textViewHeader.setText(String.format("%d. %s", position, imagen.getTitle()));
        viewHolder.textViewURL.setText(imagen.getURLIcono());

        String urlIcono = imagen.getURLIcono();
        if (Cache.iconos.containsKey(urlIcono)) {
            viewHolder.imageView.setImageBitmap(Cache.iconos.get(urlIcono));
        }
        else {
            LoadIconTask loadImageTask = new LoadIconTask(viewHolder.imageView, urlIcono);
            loadImageTask.execute();
        }
    }

    @Override
    public int getItemCount() {
        return this.imagenesList.size();
    }


    // View Holder para cada item view

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewHeader;
        TextView textViewURL;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewHeader = itemView.findViewById(R.id.textViewHeader);
            this.textViewURL = itemView.findViewById(R.id.textViewURL);
            this.imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // Evento Click para abrir actividad detalle

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Imagen imagen = imagenesList.get(position);
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("titulo", imagen.getTitle());
            intent.putExtra("url", imagen.getURLImagen());
            context.startActivity(intent);
            context.doAfterImagenClicked(imagen);
        }

        // Evento Long Click para mostrar opciones

        @Override
        public boolean onLongClick(View view) {
            int position = this.getAdapterPosition();
            Imagen imagen = imagenesList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Opciones de Imagen");
            CharSequence[] items = {"Borrar", "Share"};
            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        borrarImagen(imagen);
                        break;
                    case 1:
                        if(context.hasWritingPermission())
                            compartirImagen(imagen);
                        else
                            context.requestWritingPermissions();
                        break;
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.create().show();
            return true;
        }

        private void borrarImagen(Imagen imagen){
            Log.d("BORRAR IMAGEN: ", imagen.toString());
            imagenesList.remove(imagen);
            context.doAfterImagenBorrada(imagen);
        }

        private void compartirImagen(Imagen imagen){
            Log.d("COMPARTIR IMAGEN: ", imagen.toString());
            Bitmap bitmapIcono = Cache.iconos.get(imagen.getURLIcono());

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imagen.getId());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            OutputStream outstream;
            try {
                outstream = context.getContentResolver().openOutputStream(uri);
                bitmapIcono.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e);
            }
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share Image"));
        }
    }


    // AsyncTask para descargar iconos

    private class LoadIconTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private String urlIcono;

        public LoadIconTask(ImageView imageView, String urlIcono) {
            this.imageView = imageView;
            this.urlIcono = urlIcono;
        }

        @Override
        protected void onPreExecute(){
            Log.d("Request Icon: ", urlIcono);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmapIcono = BitmapDownloader.download(urlIcono);
            if(bitmapIcono != null)
                Cache.iconos.put(urlIcono, bitmapIcono);
            return bitmapIcono;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapIcono) {
            Log.d("Response Icon: ", urlIcono);
            this.imageView.setImageBitmap(bitmapIcono);
        }
    }

}
