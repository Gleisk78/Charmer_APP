package com.example.charmer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.charmer.tienda_recycler.Producto;
import com.example.charmer.tienda_recycler.ProductoAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Tienda extends Fragment {

    FloatingActionButton btn_anadir_producto;
    RecyclerView mRecycler;
    ProductoAdapter productoAdapter;
    FirebaseFirestore mFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View Tienda = inflater.inflate(R.layout.fragment_tienda, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = Tienda.findViewById(R.id.recyclerview_tienda);
        mRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Query query = mFirestore.collection("producto");

        FirestoreRecyclerOptions<Producto> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Producto>().setQuery(query.orderBy("fecha_añadido", Query.Direction.DESCENDING).limit(8), Producto.class).build();
        productoAdapter = new ProductoAdapter(firestoreRecyclerOptions, getActivity());
        productoAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(productoAdapter);



        btn_anadir_producto = Tienda.findViewById(R.id.btn_anadir_producto);

        btn_anadir_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Seguir = new Intent(getActivity(), Agregar_Producto.class);
                startActivity(Seguir);

            }
        });



        return Tienda;

    }


    @Override
    public void onStart() {
        super.onStart();
        mRecycler.getRecycledViewPool().clear();
        productoAdapter.notifyDataSetChanged();
        productoAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        productoAdapter.stopListening();
    }
}