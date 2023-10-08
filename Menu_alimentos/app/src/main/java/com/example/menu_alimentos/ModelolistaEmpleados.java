package com.example.menu_alimentos;

public class ModelolistaEmpleados {
    private String nombre;
    private String roll;
    private String clave;

    public ModelolistaEmpleados(String nombre, String roll, String clave) {
        this.nombre = nombre;
        this.roll = roll;
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRoll() {
        return roll;
    }

    public String getClave() {
        return clave;
    }
}

