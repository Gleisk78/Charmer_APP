package com.example.charmer;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.transform.Result;

public class nuevapublicacion extends AppCompatActivity {
    Button btn_guardar;
    ImageView btn_cancelar, nueva_imagen_post;
    EditText Input_Descripcion, Input_Etiqueta;
    Date Fecha;
    String[] Etiquetas;
    String cant_like, dsnombre, dsfoto;

    Uri path;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    ProgressDialog loading;
    FirebaseFirestore mFirestore;
    String downuri;

    private static final int codimg = 300;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregarpublicacion);


        btn_cancelar = findViewById(R.id.flecha_atras_nuevopost);
        btn_guardar = findViewById(R.id.post_guardar);
        nueva_imagen_post = findViewById(R.id.nueva_imagen_post);
        Input_Descripcion = findViewById(R.id.Input_descrip);
        Input_Etiqueta = findViewById(R.id.Input_Etiqueta);

        Fecha = new Date();

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("fotoposteo/");
        mFirestore = FirebaseFirestore.getInstance();
        loading = new ProgressDialog(nuevapublicacion.this);

        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dsnombre = documentSnapshot.getString("nombre");
                dsfoto = documentSnapshot.getString("foto");

            }
        });


        nueva_imagen_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarfoto();
            }
        });


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idusu = mAuth.getCurrentUser().getUid();
                String Imagen_usuario = dsfoto;
                String Nombre_usuario = dsnombre;
                cant_like = "0";
                String Descripcion = Input_Descripcion.getText().toString();
                String Etiqueta = Input_Etiqueta.getText().toString();
                //Etiquetas = Etiqueta.split("#");

                if(!(Descripcion.isEmpty())) {
                    if(!(Etiqueta.isEmpty())) {
                        if (path != null) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", idusu);
                            map.put("Imagen_Usuario", Imagen_usuario);
                            map.put("nombre", Nombre_usuario);
                            map.put("descripcion", Descripcion);
                            map.put("Etiquetas", Etiqueta);
                            map.put("Likes", cant_like);
                            map.put("Fecha", Fecha);
                            map.put("imagen_post", downuri);

                            mFirestore.collection("posteos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(nuevapublicacion.this, "Posteo creado", Toast.LENGTH_SHORT).show();

                                    Input_Descripcion.setText("");
                                    Input_Etiqueta.setText("");
                                    nueva_imagen_post.setBackgroundResource(R.drawable.agregarimagen);

                                    Intent Seguir = new Intent(getApplicationContext(), paginaprincipal.class);
                                    startActivity(Seguir);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(nuevapublicacion.this, "Error no se pudo crear el posteo", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {
                            Toast.makeText(nuevapublicacion.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Input_Etiqueta.setError("Ingrese mínimo una etiqueta");
                    }

                }else{
                    Input_Descripcion.setError("Ingrese una descripción");

                }
            }

        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(nuevapublicacion.this, "Publicación cancelado", Toast.LENGTH_LONG).show();
                Input_Descripcion.setText("");
                Input_Etiqueta.setText("");
                nueva_imagen_post.setBackgroundResource(R.drawable.agregarimagen);
                Intent Seguir = new Intent(getApplicationContext(), paginaprincipal.class);
                startActivity(Seguir);

            }
        });

    }

    public void cambiarfoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, codimg);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == codimg){
                path = data.getData();
                subirFoto(path);
            }
        }
    }
    


    private void subirFoto(Uri path) {
        String uniqueID = UUID.randomUUID().toString();
        loading.setTitle("Cargando imagen");
        loading.setMessage("Espere por favor...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        StorageReference reference = storageReference.child(uniqueID);
        reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(nuevapublicacion.this).load(uri).into(nueva_imagen_post);
                            downuri = uri.toString();
                            loading.dismiss();
                        }
                    });
                }
            }
        });

    }

}