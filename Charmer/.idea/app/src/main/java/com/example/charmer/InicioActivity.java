package com.example.charmer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity  extends AppCompatActivity {

    Button btnLogin, btnSing;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSing = (Button) findViewById(R.id.btn_sing);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(Volver);
            }
        });

        btnSing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(Volver);
            }
        });

    }



}
