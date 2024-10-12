package com.example.charmer.tienda_recycler;

import android.opengl.Matrix;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Producto {


    String categoria, color, Para, sexo, talla, descripcionprod;
    Date fecha_añadido;
    String idproducto;
    String idvendedor;
    ArrayList<String> imagen_prod = new ArrayList<>();
    String nombreprod;
    String precioprod;

    public Producto(){}


    public Producto(String categoria, String color, String Para, String sexo, String talla, String descripcionprod, Date fecha_añadido, String idproducto, String idvendedor, ArrayList<String> imagen_prod, String nombreprod, String precioprod) {
        this.categoria = categoria;
        this.color = color;
        this.Para = Para;
        this.sexo = sexo;
        this.talla = talla;
        this.descripcionprod = descripcionprod;
        this.fecha_añadido = fecha_añadido;
        this.idproducto = idproducto;
        this.idvendedor = idvendedor;
        this.imagen_prod = imagen_prod;
        this.nombreprod = nombreprod;
        this.precioprod = precioprod;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPara() {
        return Para;
    }

    public void setPara(String Para) {
        this.Para = Para;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getdescripcionprod() {
        return descripcionprod;
    }

    public void setdescripcionprod(String descripcionprod) {
        this.descripcionprod = descripcionprod;
    }

    public Date getfecha_añadido() {
        return fecha_añadido;
    }

    public void setfecha_añadido(Date fecha_añadido) {
        this.fecha_añadido = fecha_añadido;
    }

    public String getidproducto() {
        return idproducto;
    }

    public void setidproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getidvendedor() {
        return idvendedor;
    }

    public void setidvendedor(String idvendedor) {
        this.idvendedor = idvendedor;
    }

    public ArrayList<String> getimagen_prod() {
        return imagen_prod;
    }

    public void setimagen_prod(ArrayList<String> imagen_prod) {
        this.imagen_prod = imagen_prod;
    }

    public String getnombreprod() {
        return nombreprod;
    }

    public void setnombreprod(String nombreprod) {
        this.nombreprod = nombreprod;
    }

    public String getprecioprod() {
        return precioprod;
    }

    public void setprecioprod(String precioprod) {
        this.precioprod = precioprod;
    }





}
