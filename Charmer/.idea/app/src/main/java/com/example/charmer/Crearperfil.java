package com.example.charmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Crearperfil extends AppCompatActivity {

    ImageView btn_atras, Foto;
    Button btn_guardar;
    EditText nombre, telefono, ubicacion,ET_cif;
    TextView TV_cif;
    RadioButton RB_Ho, RB_Mu, RB_O;
    RadioGroup rgsexo;
    Uri path;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    ProgressDialog loading;
    FirebaseFirestore mFirestore;
    AwesomeValidation awval;

    private static final int codimg = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearperfil);

        btn_atras = findViewById(R.id.btn_flechaatras_perfil);
        btn_guardar = findViewById(R.id.btn_guardar_perfil);
        nombre = findViewById(R.id.ET_Fullname);
        telefono = findViewById(R.id.ET_Fono);
        ubicacion = findViewById(R.id.ET_Direccion);
        RB_Ho = findViewById(R.id.RB_man);
        RB_Mu = findViewById(R.id.RB_Women);
        RB_O = findViewById(R.id.RB_Other);
        Foto = findViewById(R.id.btn_imgload);
        rgsexo = findViewById(R.id.rgsexo);
        ET_cif = findViewById(R.id.ET_cif);
        TV_cif = findViewById(R.id.TV_cif);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("fotoPerfil");
        mFirestore = FirebaseFirestore.getInstance();
        loading = new ProgressDialog(this);

        //validacion
        awval = new AwesomeValidation(ValidationStyle.BASIC);
        awval.addValidation(Crearperfil.this, R.id.ET_Fullname, "(?=.*[a-z]).{8,}", R.string.errorname);
        awval.addValidation(Crearperfil.this, R.id.ET_Fono, Patterns.PHONE , R.string.errorfono);
        awval.addValidation(Crearperfil.this, R.id.ET_Direccion, "(?=.*[a-z])(?=.*[0-9]).{8,}", R.string.errorubi);

        //Seleccionador de empresa persona
        String tipo = getIntent().getExtras().getString("tipo");
        Toast.makeText(Crearperfil.this,tipo,Toast.LENGTH_SHORT).show();
        if(tipo.equals("persona")){
            ET_cif.setVisibility(View.GONE);
            TV_cif.setVisibility(View.GONE);
        }else if(tipo.equals("empresa")){
            awval.addValidation(Crearperfil.this, R.id.ET_cif, "(?=.*[0-9]).{8,}", R.string.errorrut);
            rgsexo.setVisibility(View.GONE);
        }

        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarfoto();
            }
        });

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Atras = new Intent(getApplicationContext(), TipoUsuario.class);
                startActivity(Atras);
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nombre.getText().toString();
                String fono = telefono.getText().toString();
                String lugar = ubicacion.getText().toString();
                String cif = ET_cif.getText().toString();
                String sexo = "";

                if(RB_Ho.isChecked())
                    sexo = "Hombre";
                else if(RB_Mu.isChecked())
                    sexo = "Mujer";
                else if(RB_O.isChecked())
                    sexo = "Otro";

                if(awval.validate()){
                    if(path != null){
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("fono",fono);
                        map.put("nombre",name);
                        map.put("sexo",sexo);
                        map.put("ubi",lugar);
                        map.put("cif",cif);
                        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Crearperfil.this,"Perfil creado",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Perfil_Persona.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Crearperfil.this,"no funciona",Toast.LENGTH_SHORT).show();
                            }
                        });
                        nombre.setText("");
                        telefono.setText("");
                        ubicacion.setText("");
                        RB_Ho.setChecked(false);
                        RB_Mu.setChecked(false);
                        RB_O.setChecked(false);
                    }else
                        Toast.makeText(Crearperfil.this,"Ingrese una imagen",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cambiarfoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, codimg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == codimg){
                path = data.getData();
                subirFoto(path);
            }
        }
    }

    private void subirFoto(Uri path) {
        loading.setTitle("Subiendo imagen...");
        loading.setMessage("Espere porfavor");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        StorageReference reference = storageReference.child(mAuth.getCurrentUser().getUid());
        reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(Crearperfil.this).load(uri).into(Foto);
                            String downuri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("foto",downuri);
                            mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loading.dismiss();
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}
