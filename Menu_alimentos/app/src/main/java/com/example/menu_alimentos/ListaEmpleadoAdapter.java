package com.example.menu_alimentos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ListaEmpleadoAdapter extends ArrayAdapter<ModelolistaEmpleados> {


    public ListaEmpleadoAdapter(Context context, List<ModelolistaEmpleados> listaRegistros) {super(context, 0, listaRegistros);}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ModelolistaEmpleados registroActual = getItem(position);


        // Comprueba si se está reutilizando una vista existente, de lo contrario, infla una nueva
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.iten_listaempleados, parent, false);
        }

        // Enlazar los elementos del diseño con los datos del registro actual
        TextView textrolli = convertView.findViewById(R.id.textrolli);
        TextView textnomi = convertView.findViewById(R.id.textnomi);
        TextView textclave = convertView.findViewById(R.id.textclave);

        textrolli.setText(registroActual.getRoll());
        textnomi.setText("  Nombre: "+ registroActual.getNombre());
        textclave.setText("  Clave: "+ registroActual.getClave());

        return convertView;
    }
}

