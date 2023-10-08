package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listaempleados extends AppCompatActivity {

    private List<ModelolistaEmpleados> listaRegistros;
    private ListView listView;

    private ListaEmpleadoAdapter adapter;

    private static String roll;
    private static String nombrel;
    private static String  idlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaempleados);


        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");


        listView = findViewById(R.id.lista_empleados);
        listaRegistros = new ArrayList<>();
        adapter = new ListaEmpleadoAdapter(this, listaRegistros);
        listView.setAdapter(adapter);

        obtenerDatosDesdePHP();
    }


    private void obtenerDatosDesdePHP() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_tablas.php";

        // Crear una nueva solicitud JSON para obtener los datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Analizar la respuesta JSON
                            JSONObject jsonObject = new JSONObject(response.toString());
                            // Obtener los arrays de las tres tablas
                            JSONArray listaCocinero = jsonObject.getJSONArray("cocinero");
                            JSONArray listaAdministrador = jsonObject.getJSONArray("administrador");
                            JSONArray listaMesero = jsonObject.getJSONArray("mesero");


                            // Agregar los registros de la tabla "administrador" a la lista
                            for (int i = 0; i < listaAdministrador.length(); i++) {
                                JSONObject item = listaAdministrador.getJSONObject(i);
                                String nombre = item.getString("nombre");
                                String clave = item.getString("clave");
                                listaRegistros.add(new ModelolistaEmpleados(nombre, "administradores", clave));
                            }

                            // Agregar los registros de la tabla "cocinero" a la lista
                            for (int i = 0; i < listaCocinero.length(); i++) {
                                JSONObject item = listaCocinero.getJSONObject(i);
                                String nombre = item.getString("nombre");
                                String clave = item.getString("clave");
                                listaRegistros.add(new ModelolistaEmpleados(nombre, "cocinero", clave));
                            }


                            // Agregar los registros de la tabla "mesero" a la lista
                            for (int i = 0; i < listaMesero.length(); i++) {
                                JSONObject item = listaMesero.getJSONObject(i);
                                String nombre = item.getString("nombre");
                                String clave = item.getString("clave");
                                listaRegistros.add(new ModelolistaEmpleados(nombre, "mesero", clave));
                            }


                            adapter.notifyDataSetChanged();
                            // Agregar un listener para el clic en los elementos del ListView
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Acciones a realizar cuando se hace clic en un elemento de la lista
                                    ModelolistaEmpleados registroSeleccionado = listaRegistros.get(position);
                                    Toast.makeText(getApplicationContext(),
                                            "Seleccionaste: " + registroSeleccionado.getRoll(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud, si es necesario
            }
        });

        // Agregar la solicitud a la cola de peticiones de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), Menu_opciones.class);
        intent.putExtra("roll", roll);
        intent.putExtra("nombrel", nombrel);
        intent.putExtra("idlog",   idlog);
        startActivity(intent);
        finish();
    }
}