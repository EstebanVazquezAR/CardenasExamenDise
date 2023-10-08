package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CocinandoOrden extends AppCompatActivity {
    private static String idorden, id_mesero, pedido,totalpedido;

    private static String roll;
    private static String nombrel;
    private static String  idlog;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocinando_orden);

        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");

        idorden = getIntent().getStringExtra("idorden");
        totalpedido = getIntent().getStringExtra("totalpedido");
        pedido = getIntent().getStringExtra("pedido");

        TextView textpedido01 = findViewById(R.id.textpedido01);
        TextView textidmesero01 = findViewById(R.id.textidmesero01);
        TextView textTotal01 = findViewById(R.id.textTotal01);

        textpedido01.setText(pedido);
        textidmesero01.setText(idlog);
        textTotal01.setText("$"+totalpedido);

        Button btnterminarcocinar = findViewById(R.id.btnTerminarpedido);
        btnterminarcocinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualiar_orden_coci();

            }
        });
    }

    private void Actualiar_orden_coci(){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/update_orden_cocinero.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        Intent intent = new Intent( CocinandoOrden.this, ListaOrdenesCocineroAut.class);
                        intent.putExtra("roll", roll);
                        intent.putExtra("nombrel", nombrel);
                        intent.putExtra("idlog",  idlog);
                        startActivity(intent);
                        finish();
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
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Click en Terminar Orden", Toast.LENGTH_SHORT).show();

    }


}