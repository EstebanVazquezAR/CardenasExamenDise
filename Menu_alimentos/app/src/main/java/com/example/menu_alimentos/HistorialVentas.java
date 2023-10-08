package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class HistorialVentas extends AppCompatActivity {

    private List<ModeloListaVentas> listaRegistros;
    private ListView listView;

    private ListaVentasAdapter adapter;

    private static String roll;
    private static String nombrel;
    private static String  idlog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);


        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");



        listView = findViewById(R.id.lista_ventas);

        listaRegistros = new ArrayList<>();
        adapter = new ListaVentasAdapter(this, listaRegistros);
        listView.setAdapter(adapter);

        obtenerDatosDesdePHP();

    }

    private void obtenerDatosDesdePHP() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_ventas.php";

        // Crear una nueva solicitud JSON para obtener los datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray listaVentas = jsonObject.getJSONArray("ventas");
                            JSONArray listaMesero = jsonObject.getJSONArray("mesero");


                            for (int i = 0; i < listaVentas.length(); i++) {
                                JSONObject vitem = listaVentas.getJSONObject(i);
                                for (int m = 0; m< listaMesero.length(); m++) {
                                    JSONObject mitem = listaMesero.getJSONObject(m);
                                    if(vitem.getString("id_mesero").equals(mitem.getString("id"))){
                                        String total = vitem.getString("Total");
                                        String mesa = vitem.getString("numero_mesa");
                                        String nombre_m = mitem.getString("nombre");
                                        listaRegistros.add(new ModeloListaVentas(nombre_m, mesa, total));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Acciones a realizar cuando se hace clic en un elemento de la lista
                                    ModeloListaVentas registroSeleccionado = listaRegistros.get(position);
                                    Toast.makeText(getApplicationContext(),
                                            "Seleccionaste: " + registroSeleccionado.getNombre_m(),Toast.LENGTH_SHORT).show();
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