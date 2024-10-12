package com.example.charmer;

import static com.example.charmer.R.id.contimagenes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Agregar_Producto extends AppCompatActivity {

    ImageView btn_Atras, imgeProd;
    Button btn_agregarIMG, btn_sig, btn_publicar;
    EditText nombreProd, precioProd, descripcionProd;
    Spinner categoriaProd, tallaProd;
    TextView contador_img;
    RadioButton mujer, hombre, vender, donar, neutro;
    int num_product = 1;
    String ABC[] = {"A", "B", "C", "D", "E"};
    int contid = 0;
    int cli = 0;
    ArrayList<String> imagenes = new ArrayList<>();

    FirebaseAuth mAuth;
    StorageReference storageReference;
    ProgressDialog loading;
    FirebaseFirestore mFirestore;
    Uri path;
    String downuri, categoriavar, tallacar;
    private static final int codimg = 300;
    int contimg = 0;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_producto_tienda);

        btn_Atras = (ImageView) findViewById(R.id.flecha_cancelar_prod);

        /// botones ///
        btn_agregarIMG = (Button)  findViewById(R.id.Elejir);
        btn_sig = findViewById(R.id.btn_tienda_sig);
        btn_publicar = findViewById(R.id.btn_publicacion); ///boton de ingresos //

        imgeProd = findViewById(R.id.Imgeprod);///imagenes//
        nombreProd = findViewById(R.id.Nombreprod);/// de aqui hasta abajo son EditsText///
        precioProd = findViewById(R.id.Precioprod);
        descripcionProd = findViewById(R.id.Descripcionprod);
        categoriaProd = (Spinner) findViewById(R.id.CategoriaProd);
        tallaProd = (Spinner) findViewById(R.id.tallaprod);
        vender = findViewById(R.id.Rvender); /// de aqui para abajo son Radiobuttons ///
        donar = findViewById(R.id.RDonar);
        mujer= findViewById(R.id.Rmujer);
        hombre = findViewById(R.id.Rhombre);
        contador_img = (TextView) findViewById(contimagenes);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        loading = new ProgressDialog(Agregar_Producto.this);
        storageReference = FirebaseStorage.getInstance().getReference().child("fotoproductos/");

        ///metodo de insercion de datos Array en el spinner///

        ArrayAdapter<CharSequence> Adapter=ArrayAdapter.createFromResource(this,
                R.array.Categorias, R.layout.modelo_spinner);

        ArrayAdapter<CharSequence> Adapter2 = ArrayAdapter.createFromResource(this, R.array.Tallas, R.layout.modelo_spinner);


        categoriaProd.setAdapter(Adapter);
        tallaProd.setAdapter(Adapter2);

        categoriaProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    categoriavar = categoriaProd.getSelectedItem().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Elija una categoria", Toast.LENGTH_SHORT).show();
            }
        });


        tallaProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tallacar = tallaProd.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Elija una talla", Toast.LENGTH_SHORT).show();
            }
        });

        btn_agregarIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contimg = imagenes.size();

                if(contimg>=3){
                    contimg = 0;
                    imagenes.remove(0);
                    imagenes.remove(1);
                    imagenes.remove(2);
                }
                cambiarfoto();
                contimg++;
                contador_img.setText("Foto: " + contimg + "/ 3");


            }
        });

        btn_sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contimg = imagenes.size(); //2

                String cargar;

                switch (cli){
                    case 1:
                        cargar = imagenes.get(1);
                        contador_img.setText("Foto: " + (cli+1) + "/ 3");

                        if (contimg == 3){
                            cli = 2;
                        }else{
                            cli = 0;

                        }


                        break;

                    case 2:
                        cargar = imagenes.get(2);
                        contador_img.setText("Foto: " + (cli+1) + "/ 3");
                        cli = 0;

                        break;

                    default:
                        cargar = imagenes.get(0);
                        contador_img.setText("Foto: " + (cli+1) + "/ 3");
                        if (contimg == 2){
                            cli = 1;
                        }else{
                            cli = 0;
                        }

                        break;

                }


                Picasso.with(Agregar_Producto.this).load(cargar).into(imgeProd);
            }
        });


        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idproducto = GenerarIDproducto();
                String idvend = mAuth.getCurrentUser().getUid();
                String nombreprod = nombreProd.getText().toString().trim();
                String precioprod = precioProd.getText().toString().trim();
                String descripcionprod = descripcionProd.getText().toString().trim();
                Date Fecha_ingresado = new Date();
                String Tipo;
                String Sexo;

                if (vender.isChecked() == true) {
                    Tipo = "Venta";
                } else if (donar.isChecked() == true) {
                    Tipo = "Donación";
                } else {
                    Tipo = "";
                }

                if (mujer.isChecked() == true) {
                    Sexo = "F";
                } else if (hombre.isChecked() == true) {
                    Sexo = "M";
                } else if (neutro.isChecked() == true) {
                    Sexo = "Neutro";
                } else {
                    Sexo = "";
                }

                if((Tipo.equals("Donación")) &&  Integer.parseInt(precioprod)>0){

                    precioprod = "0";
                }

                if (!(nombreprod.isEmpty() && precioprod.isEmpty() && descripcionprod.isEmpty())) {
                    if (!(nombreprod.isEmpty())) {
                        if (!(precioprod.isEmpty())) {
                            if(!(precioprod.length()>1000000000)  || Integer.parseInt(precioprod)<0){
                                if (!(descripcionprod.isEmpty())) {
                                    if (!(Sexo.equals(""))) {
                                        if (!(Tipo.equals(""))) {


                                            Map<String, Object> pro = new HashMap<>();
                                            pro.put("idproducto", idproducto);
                                            pro.put("idvendedor", idvend);
                                            pro.put("nombreprod", nombreprod);
                                            pro.put("precioprod", precioprod);
                                            pro.put("descripcionprod", descripcionprod);
                                            pro.put("fecha_añadido", Fecha_ingresado);
                                            pro.put("Sexo", Sexo);
                                            pro.put("Para", Tipo);
                                            pro.put("Categoria", categoriavar);
                                            pro.put("Talla", "");
                                            pro.put("Color", "");
                                            pro.put("imagen_prod", imagenes);
                                            

                                            mFirestore.collection("producto").add(pro).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getApplicationContext(), "Producto Ingresado Exitosamente", Toast.LENGTH_LONG);
                                                    Intent Seguir = new Intent(getApplicationContext(), paginaprincipal.class);
                                                    startActivity(Seguir);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Error al Ingresar", Toast.LENGTH_LONG);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Seleccione si desea vender o donar el producto", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Seleccione a que tipo de sexo pertenece el producto", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    descripcionProd.setError("Ingrese breve descripción al producto");

                                }


                            }else{
                                precioProd.setError("El monto ingresado es incorrecto, puesto que se sale del rango permitido de 0 - 1.000.000.000");
                            }

                        } else {
                            precioProd.setError("Ingrese precio al producto");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Porfavor no deje datos vacios", Toast.LENGTH_LONG).show();
                        nombreProd.setError("Ingrese nombre al producto");
                    }
                } else {
                    nombreProd.setError("Ingrese nombre al producto");
                    precioProd.setError("Ingrese precio al producto");
                    descripcionProd.setError("Ingrese breve descripción al producto");
                }


            }
        });

        btn_Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Picasso.with(Agregar_Producto.this).load(uri).into(imgeProd);
                            downuri = uri.toString();
                            imagenes.add(downuri);
                            loading.dismiss();
                        }
                    });
                }
            }
        });

    }
    public String GenerarIDproducto(){
        String ID = "";
        int validador;

        if(num_product % 100 == 0){

            if (contid == 5){
                contid = 0;
            }

            ID = String.valueOf(num_product) + ABC[contid];
            num_product++;
            contid++;

        }else{
            ID = String.valueOf(num_product) + ABC[contid];
            num_product++;

        }

        return ID;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
