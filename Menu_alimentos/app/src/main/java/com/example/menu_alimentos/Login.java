package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Login extends AppCompatActivity {

    private Button btnentrar, btncliente;
    private TextView Tusuario,Tcontra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.barcolor2));
        window.setNavigationBarColor(this.getResources().getColor(R.color.navcolor2));

        Tusuario = findViewById(R.id.edusername);
        Tcontra = findViewById(R.id.edcontral);


        btncliente = findViewById(R.id.btncliente);
        btncliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuCliente.class);
                startActivity(intent);
                finish();
            }
        });

        btnentrar = findViewById(R.id.btnentrar);
        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(), Interfazusuario.class);
                //startActivity(intent);
                if (!Tusuario.getText().toString().isEmpty() && !Tcontra.getText().toString().isEmpty()){
                    Logear_credencial(Tusuario.getText().toString(), Tcontra.getText().toString());
                }else{
                    Toast.makeText(Login.this, "Ingrese las Credenciales", Toast.LENGTH_SHORT).show();
                }

            }
        });
/*
        Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
        intent.putExtra("roll", "Mesero");
        intent.putExtra("nombrel", "jhon");
        intent.putExtra("idlog",   Integer.toString(4));
        startActivity(intent);
        finish();
*/
    }

    private void Logear_credencial(String user, String clave){

        // URL del archivo PHP que realiza la autenticación y devuelve el JSON
        String url = "https://futapp.upallanovillavicencio.com/php/menu/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("error")) {
                                // Se produjo un error en la autenticación
                                String errorMessage = jsonResponse.getString("error");
                                Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT).show();

                            } else {
                                // Autenticación exitosa
                                int idlog = jsonResponse.getInt("id");
                                String roll = jsonResponse.getString("tabla");
                                String nombre = jsonResponse.getString("nombre");
                                //Toast.makeText(getApplicationContext(), "ID: " + id + ", Roll: " + roll+ ", Validar: " + validar, Toast.LENGTH_SHORT).show();
                                if ( roll.equals("administradores")) {
                                    Toast.makeText(Login.this, "Hola "+nombre, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
                                    intent.putExtra("roll", roll);
                                    intent.putExtra("nombrel", nombre);
                                    intent.putExtra("idlog",   Integer.toString(idlog));
                                    startActivity(intent);
                                    finish();
                                }
                                else if(roll.equals("cocinero")){
                                    Toast.makeText(Login.this, "Hola "+nombre, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
                                    intent.putExtra("roll", roll);
                                    intent.putExtra("nombrel", nombre);
                                    intent.putExtra("idlog",   Integer.toString(idlog));
                                    startActivity(intent);
                                    finish();
                                }
                                else if(roll.equals("mesero")){
                                    Toast.makeText(Login.this, "Hola "+nombre, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Menu_opciones.class);
                                    intent.putExtra("roll", roll);
                                    intent.putExtra("nombrel", nombre);
                                    intent.putExtra("idlog",   Integer.toString(idlog));
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login.this, " "+roll, Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error en el formato de respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        Toast.makeText(getApplicationContext(), "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("username", user.trim());
                params.put("password", clave.trim());
                return params;
            }
        };

// Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir de MenuAPP");
        builder.setMessage("¿Estás seguro de que deseas salir?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realizar la acción para salir de la aplicación
                finishAffinity();


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