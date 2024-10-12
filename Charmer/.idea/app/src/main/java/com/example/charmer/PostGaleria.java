package com.example.charmer;

import android.net.Uri;

import java.sql.Date;
import java.sql.Timestamp;

public class PostGaleria {

    String id, Imagen_Usuario, nombre, imagen_post, Likes, descripcion, Etiquetas;
    Date fecha;

    public PostGaleria(){}


    public PostGaleria(String id, String Imagen_Usuario, String nombre, String imagen_post, String Likes, String descripcion, String Etiquetas, Date fecha) {
        this.Imagen_Usuario = Imagen_Usuario;
        this.nombre = nombre;
        this.imagen_post = imagen_post;
        this.Likes = Likes;
        this.descripcion = descripcion;
        this.Etiquetas = Etiquetas;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImagen_Usuario() {
        return Imagen_Usuario;
    }

    public void setImagen_Usuario(String Imagen_Usuario) {
        this.Imagen_Usuario = Imagen_Usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen_post() {
        return imagen_post;
    }

    public void setImagen_post(String imagen_post) {
        this.imagen_post = imagen_post;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String Likes) {
        this.Likes = Likes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEtiquetas() {
        return Etiquetas;
    }

    public void setEtiquetas(String Etiquetas) {
        this.Etiquetas = Etiquetas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
