package com.example.charmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class EditarPerfil extends AppCompatActivity {

    EditText nombre, telefono, ubicacion, cif;
    Button btn_confirm;
    RadioButton RB_Ho, RB_Mu, RB_O;
    RadioGroup grupo;
    ImageView foto;
    TextView titulocif;
    Uri path;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    ProgressDialog loading;
    StorageReference storageReference;
    AwesomeValidation awval;
    private static final int codimg = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editarperfilpersona);

        nombre = findViewById(R.id.editname);
        telefono = findViewById(R.id.editfono);
        ubicacion = findViewById(R.id.editubi);
        RB_Ho = findViewById(R.id.editman);
        RB_Mu = findViewById(R.id.editwoman);
        RB_O = findViewById(R.id.editother);
        grupo = findViewById(R.id.gruporadio);
        cif = findViewById(R.id.EdiCif);
        titulocif = findViewById(R.id.TV_CIF);
        btn_confirm = findViewById(R.id.btn_confirm);
        foto = findViewById(R.id.btn_imgloadEP);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("fotoPerfil");
        loading = new ProgressDialog(this);

        //validacion
        awval = new AwesomeValidation(ValidationStyle.BASIC);
        awval.addValidation(EditarPerfil.this, R.id.editname, "(?=.*[a-z]).{8,}", R.string.errorname);
        awval.addValidation(EditarPerfil.this, R.id.editfono, Patterns.PHONE, R.string.errorfono);
        awval.addValidation(EditarPerfil.this, R.id.editubi, "(?=.*[a-z])(?=.*[0-9]).{8,}", R.string.errorubi);

        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String sexcif;
                String dsnombre = documentSnapshot.getString("nombre");
                String dsfono = documentSnapshot.getString("fono");
                String dsubi = documentSnapshot.getString("ubi");
                String dsfoto = documentSnapshot.getString("foto");
                String dstipo = documentSnapshot.getString("tipo");
                if(dstipo.equals("Persona")) {
                    sexcif = documentSnapshot.getString("sexo");
                    cif.setVisibility(View.GONE);
                    titulocif.setVisibility(View.GONE);
                    switch (sexcif){
                        case "Hombre":{
                            RB_Ho.setChecked(true);
                            break;
                        }
                        case "Mujer":{
                            RB_Mu.setChecked(true);
                            break;
                        }
                        case "Otro":{
                            RB_O.setChecked(true);
                            break;
                        }
                    }
                }else{
                    sexcif = documentSnapshot.getString("cif");
                    grupo.setVisibility(View.GONE);
                    awval.addValidation(EditarPerfil.this, R.id.EdiCif, "(?=.*[0-9]).{8,}", R.string.errorrut);
                    cif.setText(sexcif);
                }
                nombre.setText(dsnombre);
                telefono.setText(dsfono);
                ubicacion.setText(dsubi);
                Picasso.with(EditarPerfil.this).load(dsfoto).into(foto);


            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarfoto();
            }
        });
        btn_confirm.setOnClickListener(view -> {
            String name = nombre.getText().toString();
            String fono = telefono.getText().toString();
            String lugar = ubicacion.getText().toString();
            String rut = cif.getText().toString();
            String sexo = "";

            if(RB_Ho.isChecked())
                sexo = "Hombre";
            else if(RB_Mu.isChecked())
                sexo = "Mujer";
            else if(RB_O.isChecked())
                sexo = "Otro";

            if(awval.validate()){
                HashMap<String, Object> map = new HashMap<>();
                map.put("fono",fono);
                map.put("nombre",name);
                map.put("sexo",sexo);
                map.put("ubi",lugar);
                map.put("cif",rut);
                mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditarPerfil.this,"Perfil creado",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Perfil_Persona.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarPerfil.this,"no funciona",Toast.LENGTH_SHORT).show();
                    }
                });
                nombre.setText("");
                telefono.setText("");
                ubicacion.setText("");
                RB_Ho.setChecked(false);
                RB_Mu.setChecked(false);
                RB_O.setChecked(false);
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
        loading.setTitle("Actualizando imagen...");
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
                            Picasso.with(EditarPerfil.this).load(uri).into(foto);
                            String downuri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("foto",downuri);
                            mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
