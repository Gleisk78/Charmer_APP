package com.example.charmer.tienda_recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmer.Agregar_Producto;
import com.example.charmer.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductoAdapter extends FirestoreRecyclerAdapter<Producto, ProductoAdapter.ViewHolder> {

    Activity activity;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductoAdapter(@NonNull FirestoreRecyclerOptions<Producto> options, Activity activity) {
        super(options);

        this.activity = activity;

    }

    @Override
    protected void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position, @NonNull Producto producto) {


        ArrayList<String> lista = producto.getimagen_prod();
        String imagen = lista.get(0);


        Picasso.with(holder.Imagen.getContext()).load(imagen).into(holder.Imagen);

        String Nombre = producto.getnombreprod();
        int cant_char = Nombre.length();
        if(cant_char >= 16){
            String cadena = Nombre.substring(0,14);
            cadena = cadena + "..";
            Nombre = cadena;
        }

        holder.Nombre.setText(Nombre);
        holder.precio.setText("$ " + producto.getprecioprod());
        holder.para.setText(producto.getPara());

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Seguir = new Intent(activity.getApplicationContext(), VistaProducto.class);
                activity.startActivity(Seguir);

            }
        });

    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);

        return  new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Imagen;
        TextView Nombre, precio, para;
        CardView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Imagen = itemView.findViewById(R.id.imagen_producto);
            Nombre = itemView.findViewById(R.id.id_titulo_pro);
            precio = itemView.findViewById(R.id.id_precio_pro);
            para = itemView.findViewById(R.id.id_envio_pro);
            body = itemView.findViewById(R.id.id_card_prod);
        }
    }
}
