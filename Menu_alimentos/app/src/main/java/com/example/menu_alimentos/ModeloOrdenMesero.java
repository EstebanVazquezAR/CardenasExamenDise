package com.example.menu_alimentos;

public class ModeloOrdenMesero {
    private String pedido;
    private String mesa;
    private String total;

    private int id;

    public ModeloOrdenMesero(int id, String pedido, String mesa, String total) {
        this.id = id;
        this.pedido = pedido;
        this.mesa = mesa;
        this.total = total;

    }

    public String getPedido() {
        return pedido;
    }

    public String getMesa() {
        return mesa;
    }

    public String getTotal() {
        return total;
    }

    public int getId() {
        return id;
    }



}
