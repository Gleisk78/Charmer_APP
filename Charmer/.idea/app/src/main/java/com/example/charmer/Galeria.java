package com.example.charmer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;
import java.util.List;


public class Galeria extends Fragment {

    FloatingActionButton btn_añadirpost;
    RecyclerView mRecycler;
    FirebaseFirestore mFirestore;
    RecycleViewpostAdapter RVAdapter;
    SearchView searchView;
    Query query;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View galeria = inflater.inflate(R.layout.fragment_galeria2, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = galeria.findViewById(R.id.publicaciones);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        query = mFirestore.collection("posteos");

        FirestoreRecyclerOptions<PostGaleria> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PostGaleria>().setQuery(query.orderBy("Fecha", Query.Direction.DESCENDING).limit(5),PostGaleria.class).build();
        RVAdapter = new RecycleViewpostAdapter(firestoreRecyclerOptions, getActivity());
        RVAdapter.notifyDataSetChanged();//lee cada uno
        mRecycler.setAdapter(RVAdapter);
        searchView = galeria.findViewById(R.id.search_view);
        search_View();



        btn_añadirpost =  galeria.findViewById(R.id.btn_añadirpost);

        btn_añadirpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Atras = new Intent(getActivity(), nuevapublicacion.class);
                startActivity(Atras);

            }
        });


        return galeria;
    }

    private void search_View() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                text_Search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                text_Search(s);
                return false;
            }
        });
    }

    public void  text_Search(String s){

        FirestoreRecyclerOptions<PostGaleria> firestoreRecyclerOptions;

        if(s.isEmpty()){
            firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PostGaleria>().setQuery(query.orderBy("Fecha", Query.Direction.DESCENDING).limit(3), PostGaleria.class).build();

        }else{
            firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PostGaleria>().setQuery(query.orderBy("Etiquetas").startAt(s).endAt(s + " ~ "), PostGaleria.class).build();
        }
        RVAdapter = new RecycleViewpostAdapter(firestoreRecyclerOptions, getActivity());
        RVAdapter.startListening();
        mRecycler.setAdapter(RVAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mRecycler.getRecycledViewPool().clear();
        RVAdapter.notifyDataSetChanged();
        RVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        RVAdapter.stopListening();
    }

}