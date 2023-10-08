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
import android.widget.EditText;
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

public class CambiarContrasena extends AppCompatActivity {

    private static String roll;
    private static String nombrel;
    private static String  idlog;

    public TextView Nombreset,contraset;
    private TextView titulo2;

    private Button btnActualizar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");

        titulo2= findViewById(R.id.tituloactualizar2);
        titulo2.setText("Cargo: "+roll );

        contraset= findViewById(R.id.edcontrala);
        Nombreset= findViewById(R.id.ednamecontra);
        Nombreset.setEnabled(false);
        if (Nombreset !=null){
            Nombreset.setText(nombrel.toString().trim());
        }


        btnActualizar = findViewById(R.id.btnActualizarcontra);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contraset.getText().toString().trim().isEmpty()){
                    Actualiar_contra();
                }else{
                    Toast.makeText(CambiarContrasena.this, "ingrese contraseña", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void Actualiar_contra(){
        // Realiza la lógica adicional de actualización de la tabla si es necesario
        String url = "https://futapp.upallanovillavicencio.com/php/menu/cambiar_contrasena.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
                        intent.putExtra("roll", roll);
                        intent.putExtra("nombrel",nombrel);
                        intent.putExtra("idlog", idlog);
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
                params.put("id", idlog.trim());
                params.put("roll", roll.trim());
                params.put("clave", contraset.getText().toString().trim());
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
        builder.setTitle("Cancelar Contraseña");
        builder.setMessage("¿Estás seguro de que deseas Cancelar?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( CambiarContrasena.this, Menu_opciones.class);
                intent.putExtra("idlog",idlog);
                intent.putExtra("roll",roll);
                intent.putExtra("nombrel",nombrel);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}