package com.example.menu_alimentos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;




public class ModeloMenu {
    private String nombre;
    private String tipo;
    private String precio;

    private int id;

    public ModeloMenu(int id, String nombre, String tipo, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;

    }

    public int getId() {
        return id;
    }

    public String getTipo() {

        return tipo;
    }

    public String getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }



}
