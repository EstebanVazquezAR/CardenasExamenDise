package com.example.menu_alimentos;

public class ModeloMeseroNoti {
    private String pedido;
    private String mesa;
    private String total;

    private int id;

    private int id_mesero;

    public ModeloMeseroNoti(int id,int id_mesero,  String pedido, String mesa, String total) {
        this.id_mesero = id_mesero;
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

    public int getId_mesero() {
        return id_mesero;
    }


}

