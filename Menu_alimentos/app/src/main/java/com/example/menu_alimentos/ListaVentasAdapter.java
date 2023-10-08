package com.example.menu_alimentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaVentasAdapter  extends ArrayAdapter<ModeloListaVentas> {


    public ListaVentasAdapter(Context context, List<ModeloListaVentas> listaRegistros) {super(context, 0, listaRegistros);}

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ModeloListaVentas registroActual = getItem(position);

        // Comprueba si se está reutilizando una vista existente, de lo contrario, infla una nueva
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_ventas, parent, false);
        }

        // Enlazar los elementos del diseño con los datos del registro actual
        TextView textTotal = convertView.findViewById(R.id.textTotal01);
        TextView textnombrem = convertView.findViewById(R.id.textnombrem);
        TextView textmesa = convertView.findViewById(R.id.textmesa);

        textTotal.setText("$"+registroActual.getTotal());
        textnombrem.setText("  Mesero: "+ registroActual.getNombre_m());
        textmesa.setText("  Mesa: "+ registroActual.getMesa());

        return convertView;
    }
}
