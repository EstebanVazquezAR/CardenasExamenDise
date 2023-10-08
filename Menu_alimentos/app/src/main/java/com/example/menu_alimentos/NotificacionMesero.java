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
import android.widget.Button;
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

public class NotificacionMesero extends AppCompatActivity {

    private static String roll;
    private static String nombrel;
    private static String  idlog;


    private final Handler handler = new Handler();
    private Runnable runnable;
    private RecyclerView recyclerViewListanoti;
    private ReciclerAdapterMeseroNotifi userAdapter;
    private List<ModeloMeseroNoti> userList;
    private String idorden, numero_mesa;
    private int Totalpedido;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_mesero);

        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");


        Button btnatras = findViewById(R.id.btnAtrasNoti);
        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificacionMesero.this, Menu_opciones.class);
                intent.putExtra("roll", roll);
                intent.putExtra("nombrel", nombrel);
                intent.putExtra("idlog", idlog);
                startActivity(intent);
                finish();

            }
        });


        userList = new ArrayList<>();
        recyclerViewListanoti = findViewById(R.id.recyclerViewLista03);
        recyclerViewListanoti.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new ReciclerAdapterMeseroNotifi(userList);
        recyclerViewListanoti.setAdapter(userAdapter);
        obtenerDatosDesdePHP();

        userAdapter.setOnButtonClickListener(new ReciclerAdapterMeseroNotifi.OnButtonClickListener4() {
            @Override
            public void onButtonClick(ModeloMeseroNoti menu) {
                idorden = String.valueOf(menu.getId()).trim();
                numero_mesa = menu.getMesa();
                Totalpedido  = Integer.parseInt(menu.getTotal());
                insertar_venta();
                obtenerDatosDesdePHP();
            }
        });
        runnable = new Runnable() {
            @Override
            public void run() {
                obtenerDatosDesdePHP();
                handler.postDelayed(this, 5000); // Ejecutar cada 10 segundos
            }
        };
        handler.post(runnable);
    }

    private void obtenerDatosDesdePHP() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_listamesero_noti.php";

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

                                if (idlog.trim().equals(String.valueOf(id_mesero).trim() )){
                                    String numero_mesa = vitem.getString("numero_mesa");
                                    String pedido = vitem.getString("pedido");
                                    String total = vitem.getString("total");

                                    ModeloMeseroNoti user = new ModeloMeseroNoti(id,id_mesero, pedido, numero_mesa , total);
                                    userList.add(user);
                                }else{
                                    Toast.makeText(NotificacionMesero.this, "Mesero validado", Toast.LENGTH_SHORT).show();
                                }

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

    private void insertar_venta(){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/insertar_venta.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);

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
                params.put("numero_mesa", numero_mesa.toString().trim());
                params.put("id_mesero", idlog.toString().trim());
                params.put("total", String.valueOf(Totalpedido).trim());
                params.put("idorden", idorden);
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificacionMesero.this, Menu_opciones.class);
        intent.putExtra("roll", roll);
        intent.putExtra("nombrel", nombrel);
        intent.putExtra("idlog", idlog);
        startActivity(intent);
        finish();

    }
}