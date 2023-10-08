package com.example.menu_alimentos;

public class ModeloListaVentas {
    private String nombre_m;
    private String mesa;
    private String Total;

    public ModeloListaVentas(String nombre_m, String mesa, String Total) {
        this.nombre_m = nombre_m;
        this.mesa = mesa;
        this.Total = Total;
    }

    public String getNombre_m() {
        return nombre_m;
    }

    public String getMesa() {
        return mesa;
    }

    public String getTotal() {
        return Total;
    }
}
