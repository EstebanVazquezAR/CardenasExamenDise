package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

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

public class ListaOrdenesCocineroAut extends AppCompatActivity {
    private final Handler handler = new Handler();
    private Runnable runnable;

    private RecyclerView recyclerViewListacocinero;
    private RecicclereAdaptadorOrdenCocinero userAdapter;
    private List<ModeloOrdenCocinero> userList;

    private String idorden;

    private static String roll;
    private static String nombrel;
    private static String  idlog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordenes_cocinero_aut);


        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");

        Button btnSALIR = findViewById(R.id.btnSalirlistcosiorden);
        btnSALIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListaOrdenesCocineroAut.this);
                builder.setTitle("Desea Salir");
                builder.setMessage("¿Estás seguro de que deseas SALIR?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Realizar la acción para salir de la aplicación
                        Intent intent = new Intent( ListaOrdenesCocineroAut.this, Menu_opciones.class);
                        intent.putExtra("roll", roll);
                        intent.putExtra("nombrel", nombrel);
                        intent.putExtra("idlog",  idlog);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }


        });

        userList = new ArrayList<>();
        recyclerViewListacocinero = findViewById(R.id.recyclerViewLista02);
        recyclerViewListacocinero.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new RecicclereAdaptadorOrdenCocinero(userList);
        recyclerViewListacocinero.setAdapter(userAdapter);
        obtenerDatosDesdePHP();

        userAdapter.setOnButtonClickListener(new RecicclereAdaptadorOrdenCocinero.OnButtonClickListener3() {
            @Override
            public void onButtonClick(ModeloOrdenCocinero menu) {
                obtenerDatosDesdePHP();
                idorden = String.valueOf(menu.getId());
                String id_mesero = String.valueOf(menu.getId_mesero());
                String pedido = menu.getPedido();
                String totalpedido = menu.getTotal();



                Intent intent = new Intent( ListaOrdenesCocineroAut.this, CocinandoOrden.class);
                intent.putExtra("roll", roll);
                intent.putExtra("nombrel", nombrel);
                intent.putExtra("idlog",  idlog);

                intent.putExtra("idorden", idorden);
                intent.putExtra("pedido", pedido);
                intent.putExtra("totalpedido",  totalpedido);
                startActivity(intent);
                finish();

            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                obtenerDatosDesdePHP();
                handler.postDelayed(this, 6000); // Ejecutar cada 10 segundos
            }
        };
        handler.post(runnable);
    }

    private void obtenerDatosDesdePHP() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_listacocinero_orden.php";

        // Crear una nueva solicitud JSON para obtener los datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray listaordenes = jsonObject.getJSONArray("orden");
                            userAdapter.clearList();
                            for (int i = 0; i < listaordenes.length(); i++) {
                                JSONObject vitem = listaordenes.getJSONObject(i);
                                int id = vitem.getInt("id");
                                int id_mesero = vitem.getInt("id_mesero");
                                String numero_mesa = vitem.getString("numero_mesa");
                                String pedido = vitem.getString("pedido");
                                String total = vitem.getString("total");
                                ModeloOrdenCocinero user = new ModeloOrdenCocinero(id,id_mesero, pedido, numero_mesa , total);
                                userList.add(user);
                            }

                            userAdapter.notifyDataSetChanged();

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

}