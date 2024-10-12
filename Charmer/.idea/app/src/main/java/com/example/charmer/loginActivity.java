package com.example.charmer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class loginActivity extends AppCompatActivity {

    EditText correo, password;
    TextView Registrarse,link_olvidar_contraseña;
    Button ingresar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        correo = (EditText) findViewById(R.id.input_correo);
        password = (EditText) findViewById(R.id.input_password);
        Registrarse = (TextView) findViewById(R.id.linkRegistrar);
        ingresar = (Button) findViewById(R.id.btn_ingreso);
        link_olvidar_contraseña = findViewById(R.id.link_olvidar_contraseña);

        mAuth = FirebaseAuth.getInstance();

        link_olvidar_contraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), resetpass.class);
                startActivity(intent);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = correo.getText().toString();
                String pass = password.getText().toString();
                Integer c = 0;

                if(TextUtils.isEmpty(user)){
                    correo.setError("Rellene los datos");
                    correo.requestFocus();
                }else
                    c++;
                if(TextUtils.isEmpty(pass)){
                    password.setError("Rellene los datos");
                    password.requestFocus();
                }else
                    c++;
                if(c == 2){
                    mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), paginaprincipal.class);
                                startActivity(intent);
                            }else{
                                String error = ((FirebaseAuthException)task.getException()).getErrorCode();
                                dameToastdeerror(error);
                            }
                        }
                    });
                }
            }
        });

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Registrar = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(Registrar);
            }
        });
    }
    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                correo.setError("La dirección de correo electrónico está mal formateada.");
                correo.requestFocus();
                break;
            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                password.setError("la contraseña es incorrecta ");
                password.requestFocus();
                break;
            case "ERROR_USER_MISMATCH":
                Toast.makeText(this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                correo.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                correo.requestFocus();
                break;
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_DISABLED":
                Toast.makeText(this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                password.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                password.requestFocus();
                break;
        }
    }
}
