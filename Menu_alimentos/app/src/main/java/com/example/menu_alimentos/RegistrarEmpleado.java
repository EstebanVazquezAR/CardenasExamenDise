package com.example.menu_alimentos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class RegistrarEmpleado extends AppCompatActivity {

    private Spinner spinnerRoles;
    private String selectedRole;
    private Button btnregistrar;


    public TextView Nombreingre,contraingre;

    private static String roll;
    private static String nombrel;
    private static String  idlog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_empleado);
        Spinerfun();

        roll = getIntent().getStringExtra("roll");
        nombrel = getIntent().getStringExtra("nombrel");
        idlog = getIntent().getStringExtra("idlog");



        contraingre= findViewById(R.id.edcontralr);
        Nombreingre= findViewById(R.id.edusernamer);
        btnregistrar = findViewById(R.id.btnRegistrar01);
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });

    }

    private void Spinerfun(){
        spinnerRoles = findViewById(R.id.spinnerempleado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);

    }

    private void Registrar(){
        selectedRole = spinnerRoles.getSelectedItem().toString();
        if (!contraingre.getText().toString().isEmpty() && !Nombreingre.getText().toString().isEmpty() ){
            insertarEquipo();
        }else {

            Toast.makeText(this, "Ingrese Los datos", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertarEquipo(){


        String url = "https://futapp.upallanovillavicencio.com/php/menu/insertar_credencial.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Maneja la respuesta del servidor
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
                        intent.putExtra("roll", roll);
                        intent.putExtra("nombrel", nombrel);
                        intent.putExtra("idlog", idlog);
                        startActivity(intent);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la solicitud
                        Toast.makeText(getApplicationContext(),  "Error insertar", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pasa los parámetros necesarios al archivo PHP
                Map<String, String> params = new HashMap<>();
                params.put("roll", selectedRole);
                params.put("nombre", Nombreingre.getText().toString());
                params.put("contrasena", contraingre.getText().toString().trim());

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
        builder.setTitle("Cancelar Registro");
        builder.setMessage("¿Estás seguro de que deseas Cancelar?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realizar la acción para salir de la aplicación
                Intent intent = new Intent( getApplicationContext(), Menu_opciones.class);
                intent.putExtra("roll", roll);
                intent.putExtra("nombrel", nombrel);
                intent.putExtra("idlog",   idlog);
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