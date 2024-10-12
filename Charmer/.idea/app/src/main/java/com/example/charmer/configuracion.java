package com.example.charmer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class configuracion extends AppCompatActivity {

    Button Perfil, Donaciones, Tienda, Manual, Exit;
    ImageView Atras, foto;
    TextView nombre;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);

        Perfil = (Button) findViewById(R.id.button_irperfil);
        Donaciones = (Button) findViewById(R.id.button_irmisdonaciones);
        Tienda = (Button) findViewById(R.id.button_irtienda);
        Manual = (Button) findViewById(R.id.button_irmanual);
        Exit = (Button) findViewById(R.id.button_ircerrar);
        Atras = (ImageView) findViewById(R.id.btn_flechaMP);
        foto = findViewById(R.id.imgperfilpersonaP);
        nombre =  (TextView) findViewById(R.id.TV_Nombre_C);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dsfoto = documentSnapshot.getString("foto");
                Picasso.with(configuracion.this).load(dsfoto).into(foto);
            }
        });

        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), Perfil_Persona.class);
                startActivity(ir);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), InicioActivity.class);
                mAuth.signOut();
                startActivity(ir);
            }
        });

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), paginaprincipal.class);
                startActivity(ir);
            }
        });



    }

}
