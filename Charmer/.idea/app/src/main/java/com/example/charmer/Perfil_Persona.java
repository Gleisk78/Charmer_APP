package com.example.charmer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;

public class Perfil_Persona extends AppCompatActivity {

    ImageView btn_atras,foto;
    Button btn_editar;
    TextView nombre, correo, telefono, ubicacion, sexo, SexoCif;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_persona);

        btn_atras = (ImageView) findViewById(R.id.btn_flechaatras_perfil_MP);
        btn_editar = (Button) findViewById(R.id.btn_EditarPerfil);
        nombre = (TextView) findViewById(R.id.TV_Nombre);
        correo = (TextView) findViewById(R.id.correo_usuario);
        telefono = (TextView) findViewById(R.id.telefono_usuario);
        ubicacion = (TextView) findViewById(R.id.ubicacion_usuario);
        sexo = (TextView) findViewById(R.id.Sexo_Usuario);
        SexoCif = (TextView) findViewById(R.id.Sexo_Cif);
        foto = findViewById(R.id.imgperfilpersonaP);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String sexcif;
                String dsnombre = documentSnapshot.getString("nombre");
                String dsfono = documentSnapshot.getString("fono");
                String dsubi = documentSnapshot.getString("ubi");
                String dsfoto = documentSnapshot.getString("foto");
                String dstipo = documentSnapshot.getString("tipo");
                String dsmail = user.getEmail();
                if(dstipo.equals("Persona")) {
                    sexcif = documentSnapshot.getString("sexo");
                    SexoCif.setText("Sexo");
                }else {
                    sexcif = documentSnapshot.getString("cif");
                    SexoCif.setText("RUT");
                }
                nombre.setText(dsnombre);
                correo.setText(dsmail);
                telefono.setText(dsfono);
                ubicacion.setText(dsubi);
                sexo.setText(sexcif);
                Picasso.with(Perfil_Persona.this).load(dsfoto).into(foto);
            }
        });

        btn_editar.setOnClickListener(view ->{
            Intent editar = new Intent(getApplicationContext(), EditarPerfil.class);
            startActivity(editar);
        });



        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Atras = new Intent(getApplicationContext(), configuracion.class);
                startActivity(Atras);
            }
        });

    }
}