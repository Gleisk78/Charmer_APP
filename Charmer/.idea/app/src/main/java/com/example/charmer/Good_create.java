package com.example.charmer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Good_create extends AppCompatActivity {

    Button btn_CrearPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_create);

        btn_CrearPerfil = findViewById(R.id.btn_crearperfil);

        btn_CrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Seguir = new Intent(getApplicationContext(), TipoUsuario.class);
                startActivity(Seguir);
            }
        });
    }
}
