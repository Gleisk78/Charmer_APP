package com.example.charmer.tienda_recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charmer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VistaProducto extends AppCompatActivity {

    FloatingActionButton btn_atras;
    ImageView btn_imagen_producto, perfil_usu;
    TextView Nombre_Producto, Precio,  Nombre_Usu,  color_p, talla_p, sexo_p, categoria_p, descripcion_p, tiempo_entrega;
    Button btn_Comprar, btn_Añadir_Carrito;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_producto);

        btn_imagen_producto = findViewById(R.id.imagen_producto_vista);
        perfil_usu = findViewById(R.id.perfil_usu_vistaprod);
        Nombre_Producto = findViewById(R.id.titulo_producto_vista);
        Precio = findViewById(R.id.precio_producto_vista);
        Nombre_Usu = findViewById(R.id.nombre_usu_vista);
        color_p = findViewById(R.id.id_color_prod_v);
        talla_p = findViewById(R.id.id_talla_prov);
        sexo_p = findViewById(R.id.id_sex_prov);
        categoria_p = findViewById(R.id.id_cat_prodv);
        descripcion_p = findViewById(R.id.id_descripcion_prodv);
        tiempo_entrega = findViewById(R.id.tiempo_de_envio);
        btn_Comprar = findViewById(R.id.btn_comprar_producto);
        btn_Añadir_Carrito = findViewById(R.id.btn_agregar_al_carrito);
        btn_atras =  findViewById(R.id.btn_regreso_tienda);


    }
}
