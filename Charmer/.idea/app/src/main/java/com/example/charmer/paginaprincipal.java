package com.example.charmer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class paginaprincipal extends AppCompatActivity {

    ImageView configuracion,viewTiempo;
    TextView tituloV,numTiempo;

    Galeria GaleriaF = new Galeria();
    Armario ArmarioF = new Armario();
    myoutfit MyOutfitF = new myoutfit();
    calendario CalendarioF = new calendario();
    Tienda TiendaF = new Tienda();

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    //Tiempo api
    private final static int request = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final String urlTiempo = "https://api.openweathermap.org/data/2.5/weather";
    private final String urlIcontiempo = "http://openweathermap.org/img/wn/";
    private final String appid = "3d3c5e558e7c5fb820cc933c06a92194";
    private String tempUrl = "";
    private Double lat=0.0,lon=0.0;
    DecimalFormat df = new DecimalFormat("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_principal);

        configuracion = findViewById(R.id.boton_salir);
        tituloV = findViewById(R.id.TV_ventana);

        //Tiempo
        viewTiempo = findViewById(R.id.viewTiempo);
        numTiempo = findViewById(R.id.numTiempo);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        clima();

        BottomNavigationView navegation = findViewById(R.id.bottomNavigationView);
        navegation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tituloV.setText("Galeria");
        loadFragment(GaleriaF);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        mFirestore.collection("perfil").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dsfoto = documentSnapshot.getString("foto");
                Picasso.with(paginaprincipal.this).load(dsfoto).into(configuracion);
            }
        });

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Atras = new Intent(getApplicationContext(), configuracion.class);
                startActivity(Atras);
            }
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.menu_home:
                    tituloV.setText("Galeria");
                    loadFragment(GaleriaF);
                    return true;

                case R.id.menu_armario:
                    tituloV.setText("Armario");
                    loadFragment(ArmarioF);
                    return true;

                case R.id.crear_outfit:
                    tituloV.setText("MyOutfit");
                    loadFragment(MyOutfitF);
                    return true;

                case R.id.menu_agenda:
                    tituloV.setText("Calendario");
                    loadFragment(CalendarioF);
                    return true;

                case R.id.menu_tienda:
                    tituloV.setText("Tienda");
                    loadFragment(TiendaF);
                    return true;
            }
            return false;
        }
    };

    public  void  loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framegrupo, fragment);
        transaction.commit();
    }

    public void clima(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        Geocoder geocoder = new Geocoder(paginaprincipal.this, Locale.getDefault());
                        List<Address> addressList= null;
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            lat = addressList.get(0).getLatitude();
                            lon = addressList.get(0).getLongitude();
                            tempUrl = urlTiempo+"?lat="+lat+"&lon="+lon+"&appid="+appid;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Log.d("response",response);
                                    try {
                                        JSONObject jsonresponse = new JSONObject(response);
                                        JSONArray jsonArray = jsonresponse.getJSONArray("weather");
                                        JSONObject jsonObjectClima = jsonArray.getJSONObject(0);
                                        String icon = jsonObjectClima.getString("icon");
                                        JSONObject jsonObjectMain = jsonresponse.getJSONObject("main");
                                        double temp = jsonObjectMain.getDouble("temp")-273.15;
                                        numTiempo.setText(df.format(temp)+"°C");
                                        Picasso.with(paginaprincipal.this).load(urlIcontiempo+icon+"@2x.png").into(viewTiempo);
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_LONG).show();
                                }
                            });
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            askPermission();
        }
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(paginaprincipal.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==request){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                clima();
            }else{
                Toast.makeText(paginaprincipal.this,"Permiso requerido",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void RedirecionarFragment(int pag){
        switch (pag){
            case 0:
                tituloV.setText("Galeria");
                loadFragment(GaleriaF);

            case 1:
                tituloV.setText("Armario");
                loadFragment(ArmarioF);

            case 2:
                tituloV.setText("MyOutfit");
                loadFragment(MyOutfitF);


            case 3:
                tituloV.setText("Calendario");
                loadFragment(CalendarioF);


            case 4:
                tituloV.setText("Tienda");
                loadFragment(TiendaF);

            default:
                tituloV.setText("Galeria");
                loadFragment(GaleriaF);
        }

    }
}
