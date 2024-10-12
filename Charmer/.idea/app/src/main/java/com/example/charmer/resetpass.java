package com.example.charmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpass extends AppCompatActivity {

    EditText correoreset;
    ImageView atrasreset;
    Button btn_enviarmail;

    FirebaseAuth mAuth;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        correoreset = findViewById(R.id.correoreset);
        atrasreset = findViewById(R.id.atrasreset);
        btn_enviarmail = findViewById(R.id.btn_enviarmail);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        atrasreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
            }
        });
        btn_enviarmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = correoreset.getText().toString();
                if(!mail.isEmpty()){
                    loader.setMessage("Espere un momento...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.setLanguageCode("es");
                    mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(resetpass.this, "Se ha enviado un correo para restablecer tu contraseña", Toast.LENGTH_LONG).show();
                            }else
                                Toast.makeText(resetpass.this, "No se pudo enviar el correo", Toast.LENGTH_LONG).show();
                            loader.dismiss();
                        }
                    });
                }else
                    correoreset.setError("Ingrese un email");
            }
        });
    }
}