package com.example.charmer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    EditText correo, password, confpass;
    Button btn_crear;
    TextView link_login;

    FirebaseAuth mAuth;
    AwesomeValidation awval;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        correo = (EditText) findViewById(R.id.input_correo_crear);
        password = (EditText) findViewById(R.id.password_crear);
        confpass = (EditText) findViewById(R.id.confpass_crear);
        btn_crear = (Button) findViewById(R.id.btn_crear_cuenta);
        link_login = (TextView) findViewById(R.id.linkLogin);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        awval = new AwesomeValidation(ValidationStyle.BASIC);
        awval.addValidation(this, R.id.input_correo_crear, android.util.Patterns.EMAIL_ADDRESS, R.string.errormail);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awval.addValidation(this, R.id.password_crear, regexPassword, R.string.errorpass);
        awval.addValidation(this, R.id.confpass_crear, R.id.password_crear, R.string.errorconpass);

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = correo.getText().toString();
                String contrasena = password.getText().toString();
                String repass = confpass.getText().toString();

                correo.setError(null);
                password.setError(null);
                confpass.setError(null);
                Integer c = 0;

                if(TextUtils.isEmpty(user)) {
                    correo.setError("Rellene los datos");
                    correo.requestFocus();
                }else
                    c++;
                if(TextUtils.isEmpty(contrasena)) {
                    password.setError("Rellene los datos");
                    password.requestFocus();
                }else
                    c++;
                if(TextUtils.isEmpty(repass)) {
                    confpass.setError("Rellene los datos");
                    confpass.requestFocus();
                }else
                    c++;
                if(c == 3){
                    if(awval.validate()){
                        mAuth.createUserWithEmailAndPassword(user,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(), Good_create.class);
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("ass","");
                                    mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).set(map);
                                    startActivity(intent);
                                    correo.setText("");
                                    password.setText("");
                                    confpass.setText("");
                                }else{
                                    String error = ((FirebaseAuthException)task.getException()).getErrorCode();
                                    dameToastdeerror(error);
                                }
                            }
                        });
                    }
                }
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),loginActivity.class);
                startActivity(intent);
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

