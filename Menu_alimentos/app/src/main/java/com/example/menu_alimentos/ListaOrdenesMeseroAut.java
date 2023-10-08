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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaOrdenesMeseroAut extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Runnable runnable;

    private RecyclerView recyclerViewListaMeseroAU;
    private ReciclerAdaptadorOrdenmesero userAdapter;
    private List<ModeloOrdenMesero> userList;

    private String idorden;

    private static String roll;
    private static String nombrel;
    private static String  idlog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordenes_mesero_aut);

        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");

        Button btnSALIR = findViewById(R.id.btnSalirlistmeseroaut);
        btnSALIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListaOrdenesMeseroAut.this);
                    builder.setTitle("Desea Salir");
                    builder.setMessage("¿Estás seguro de que deseas SALIR?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Realizar la acción para salir de la aplicación
                            Intent intent = new Intent( ListaOrdenesMeseroAut.this, Menu_opciones.class);
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
        recyclerViewListaMeseroAU = findViewById(R.id.recyclerViewLista);
        recyclerViewListaMeseroAU.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new ReciclerAdaptadorOrdenmesero(userList);
        recyclerViewListaMeseroAU.setAdapter(userAdapter);
        obtenerDatosDesdePHP();


        userAdapter.setOnButtonClickListener(new ReciclerAdaptadorOrdenmesero.OnButtonClickListener2() {
            @Override
            public void onButtonClick(ModeloOrdenMesero menu) {
                idorden = String.valueOf(menu.getId());
                Actualiar_orden();
                obtenerDatosDesdePHP();
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
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_listamesero_aut.php";

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
                                String numero_mesa = vitem.getString("numero_mesa");
                                String pedido = vitem.getString("pedido");
                                String total = vitem.getString("total");
                                ModeloOrdenMesero user = new ModeloOrdenMesero(id, pedido, numero_mesa , total);
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

    private void Actualiar_orden(){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/update_orden_mesero.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        obtenerDatosDesdePHP();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la solicitud
                        Toast.makeText(getApplicationContext(),  "Error insertar"+ error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pasa los parámetros necesarios al archivo PHP
                Map<String, String> params = new HashMap<>();
                params.put("idorden", idorden.toString().trim());
                params.put("idmesero", idlog.toString().trim());
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NO ORDENAR");
            builder.setMessage("¿Estás seguro de que deseas salir?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
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

}