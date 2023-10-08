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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MenuCliente extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Runnable runnable;
    private Button btnOrdenar,btnSALIR,btnreOrdenar, btncomi,btnbebi,btnantojos;
    private TextView HoraReal, Totalvista, mensajeConfig, edit_text_menu_mesa;
    private RecyclerView recyclerViewmenu;
    private RecyclerAdaptadorMenu userAdapter;
    private List<ModeloMenu> userList;
    private Spinner spinnermesa;
    private String selectedRole, numerode_mesa,pedidoCompleto="";
    ArrayList<PlatoInfo> platosInfo = new ArrayList<>();
    private int Totalorden;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cliente);
        HoraReal = findViewById(R.id.horaytipo);

        mensajeConfig = findViewById(R.id.mensajeCONFI);
        Totalvista = findViewById(R.id.edit_text_menu);
        spinnermesa = findViewById(R.id.spinnermesa);
        edit_text_menu_mesa = findViewById(R.id.edit_text_menu_mesa);
        LLenarSpinnerMesas();


        userList = new ArrayList<>();
        recyclerViewmenu = findViewById(R.id.recyclerViewLista);
        recyclerViewmenu.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new RecyclerAdaptadorMenu(userList);
        recyclerViewmenu.setAdapter(userAdapter);

        btnSALIR = findViewById(R.id.btnSalirmenu);
        btnSALIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnOrdenar.isEnabled() ){
                    Toast.makeText(MenuCliente.this, "Espera la orden esta en proceso", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuCliente.this);
                    builder.setTitle("Desea Cerrar");
                    builder.setMessage("¿Estás seguro de que deseas Cerrar?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Realizar la acción para salir de la aplicación
                            Intent intent = new Intent( MenuCliente.this, Login.class);
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

        });


        btnOrdenar = findViewById(R.id.btnORDENAR);
        btnOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = spinnermesa.getSelectedItem().toString();
                if ( Totalorden>0 && Integer.parseInt(selectedRole) >0){
                    Prepedido();
                    generarOrden();
                    btnreOrdenar.setEnabled(true);

                }else {
                    Toast.makeText(MenuCliente.this, "No hay pedido o mesas", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnreOrdenar = findViewById(R.id.btnORDENARdenuevo);
        btnreOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Prepedido();
                Actualiar_mesa_orden();

            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                actualizarHora();
                LLenarSpinnerMesas();
                if (!btnOrdenar.isEnabled()){
                    ValidarEntrEgaCliente();
                }
                handler.postDelayed(this, 5000); // Ejecutar cada 10 segundos
            }
        };
        handler.post(runnable);
        Load_data();

        userAdapter.setOnButtonClickListener(new RecyclerAdaptadorMenu.OnButtonClickListener() {
            @Override
            public void onButtonClick(ModeloMenu menu) {
                int menuid = menu.getId();
                String menuName = menu.getNombre();
                String menutipo = menu.getTipo();
                String menuprecio = menu.getPrecio();
                Totalorden += Integer.parseInt(menuprecio) ;
                Totalvista.setText( String.valueOf(Totalorden));

                // Busca si el plato ya existe en el ArrayList
                boolean platoExistente = false;
                for (PlatoInfo platoInfo : platosInfo) {
                    if (platoInfo.getMenuid() == menuid) {
                        platoInfo.setCantidad(platoInfo.getCantidad() + 1);
                        platoExistente = true;
                        break;
                    }
                }

                // Si no existe, agrega uno nuevo al ArrayList
                if (!platoExistente) {
                    PlatoInfo nuevoPlato = new PlatoInfo(menuid, menuName, menutipo, menuprecio, 1); // Configuramos la cantidad inicial a 1 para el nuevo plato
                    platosInfo.add(nuevoPlato);
                }

            }

        });

        btncomi = findViewById(R.id.btncomidas01);
        btncomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                Load_data();

            }
        });

        btnbebi = findViewById(R.id.btnbebidas01);
        btnbebi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                Load_data_bebidas();

            }
        });
        btnantojos = findViewById(R.id.btnantojos01);
        btnantojos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                Load_data_antojos();

            }
        });
    }
     private void Prepedido(){
         StringBuilder pedidoCompletoBuilder = new StringBuilder();

         for (PlatoInfo platoInfo : platosInfo) {
             int cantidad = platoInfo.getCantidad();
             String platoDetails = "Nombre: " + platoInfo.getNombre() + " - Precio: " + platoInfo.getPrecio();
             pedidoCompletoBuilder.append(" / ").append(platoDetails).append(" / Cantidad: ").append(cantidad);
         }

         pedidoCompleto = pedidoCompletoBuilder.toString();


         Toast.makeText(MenuCliente.this,  pedidoCompleto, Toast.LENGTH_SHORT).show();
     }
    //clase para manejar el numero de paltos y aumentar su cantidad
    public class PlatoInfo {
        private int menuid;
        private String nombre;
        private String tipo;
        private String precio;

        private int cantidad;


        public PlatoInfo(int menuid, String nombre, String tipo, String precio, int cantidad) {
            this.menuid = menuid;
            this.nombre = nombre;
            this.tipo = tipo;
            this.precio = precio;
            this.cantidad = cantidad;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public int getMenuid() {
            return menuid;
        }

        public void setMenuid(int menuid) {
            this.menuid = menuid;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getPrecio() {
            return precio;
        }

        public void setPrecio(String precio) {
            this.precio = precio;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la actualización de la hora al cerrar la actividad
        handler.removeCallbacks(runnable);
    }
    //nueva actualizamos la mesa de la orden nueva adicionada
    private void Actualiar_mesa_orden(){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/update_orden_reorden.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        LLenarSpinnerMesas();
                        bloquearorden();
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
                params.put("total", String.valueOf(Totalorden));
                params.put("pedido", pedidoCompleto);
                params.put("numero_mesa", selectedRole.toString().trim());
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
    private void generarOrden() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/insertar_orden.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);

                        Actualiar_mesa();
                        edit_text_menu_mesa.setText("#"+selectedRole.toString().trim());
                        mensajeConfig.setText("Orden Generada");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),  "Error insertar", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pasa los parámetros necesarios al archivo PHP
                Map<String, String> params = new HashMap<>();
                params.put("total", String.valueOf(Totalorden));
                params.put("pedido", pedidoCompleto);
                params.put("numero_mesa", selectedRole.toString().trim());

                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
    private void Actualiar_mesa(){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/update_mesa_orden.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        Log.d("errmysql", response);
                        LLenarSpinnerMesas();
                        bloquearorden();
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
                params.put("numero_mesa", selectedRole.toString().trim());
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
    @SuppressLint("SetTextI18n")
    private void bloquearorden(){
        spinnermesa.setEnabled(false);
        btnOrdenar.setEnabled(false);
        btnreOrdenar.setEnabled(true);
        btnOrdenar.setText("Enviada");
       // mensajeConfig.setText("espera...");
    }
    private void LLenarSpinnerMesas() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/llenar_spiner_mesas.php";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> opciones = new ArrayList<>();
                            ArrayList<String> elementos = new ArrayList<>();
                            // Recorrer el JSONArray y obtener los datos deseados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String numero = jsonObject.getString("numero");
                                elementos.add(numero);

                            }

                            // Crea un adaptador para el Spinner usando el ArrayList
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MenuCliente.this, android.R.layout.simple_spinner_item, elementos);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnermesa.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MenuCliente.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    @SuppressLint("SetTextI18n")
    private void actualizarHora() {

        TimeZone timeZone = TimeZone.getTimeZone("America/Mexico_City");
        TimeZone.setDefault(timeZone);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Formato para mostrar la hora en formato de 24 horas
        String horaActualMexico = sdf.format(date);


        // Mostrar la hora en la TextView
        HoraReal.setText("Hora actual en México: " + horaActualMexico);
    }
    private void Load_data(){

        // URL del archivo PHP que realiza la autenticación y devuelve el JSON
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_menu_tipo.php";;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() <=0){
                                Toast.makeText(MenuCliente.this, "Fuera de servicio", Toast.LENGTH_SHORT).show();
                            }
                            // Procesar cada elemento del menú
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject elemento = jsonArray.getJSONObject(i);

                                // Obtener los datos de cada elemento del menú
                                int id = elemento.getInt("id");
                                String tipo = elemento.getString("tipo");
                                String nombre = elemento.getString("nombre");
                                String precio = elemento.getString("precio");
                                ModeloMenu user = new ModeloMenu(id, nombre, tipo , precio);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores en caso de que ocurra alguno
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Obtener la hora actual en formato "HH:mm"
                TimeZone timeZone = TimeZone.getTimeZone("America/Mexico_City");
                TimeZone.setDefault(timeZone);

                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Formato para mostrar la hora en formato de 24 horas
                String horaActualMexico = sdf.format(date);

                // Agregar el parámetro 'hora' a la solicitud POST
                params.put("hora", horaActualMexico);
                //params.put("hora", "20:00");
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request);

    }
    private void Load_data_bebidas(){

        // URL del archivo PHP que realiza la autenticación y devuelve el JSON
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_bebidas.php";;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() <=0){
                                Toast.makeText(MenuCliente.this, "Fuera de servicio", Toast.LENGTH_SHORT).show();
                            }
                            // Procesar cada elemento del menú
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject elemento = jsonArray.getJSONObject(i);

                                // Obtener los datos de cada elemento del menú
                                int id = elemento.getInt("id");
                                String nombre = elemento.getString("nombre");
                                String precio = elemento.getString("precio");
                                ModeloMenu user = new ModeloMenu(id, nombre, "bebida" , precio);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores en caso de que ocurra alguno
                    }
                }) {

        };

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request);

    }
    private void Load_data_antojos(){
        // URL del archivo PHP que realiza la autenticación y devuelve el JSON
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_antojos.php";;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() <=0){
                                Toast.makeText(MenuCliente.this, "Fuera de servicio", Toast.LENGTH_SHORT).show();
                            }
                            // Procesar cada elemento del menú
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject elemento = jsonArray.getJSONObject(i);

                                // Obtener los datos de cada elemento del menú
                                int id = elemento.getInt("id");
                                String nombre = elemento.getString("nombre");
                                String precio = elemento.getString("precio");
                                ModeloMenu user = new ModeloMenu(id, nombre, "antojos" , precio);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores en caso de que ocurra alguno
                    }
                }) {

        };

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request);

    }
    private void ValidarEntrEgaCliente() {
        String url = "https://futapp.upallanovillavicencio.com/php/menu/consultar_lista_entrega.php";

        // Crear una nueva solicitud JSON para obtener los datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray listaordenes = jsonObject.getJSONArray("orden");


                            for (int i = 0; i < listaordenes.length(); i++) {
                                JSONObject vitem = listaordenes.getJSONObject(i);
                                String numero_mesa = vitem.getString("numero_mesa");

                                if (selectedRole !=null && !btnOrdenar.isEnabled()){
                                    if (numero_mesa.trim().equals(selectedRole.trim()) ){
                                        mensajeConfig.setText("Pueder cerrar la mesa cuando termines tu mesa a sido liberada");
                                        btnOrdenar.setText("Ordenar");
                                        btnOrdenar.setEnabled(true);
                                        spinnermesa.setEnabled(true);
                                        Totalvista.setText("Total");
                                        Totalorden = 0;
                                        edit_text_menu_mesa.setText("Mesa");
                                        insertar_mesa_hilo(selectedRole);
                                        pedidoCompleto="";
                                        platosInfo.clear();
                                    }
                                }

                            }



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
    private void insertar_mesa_hilo(String numeromesarece){
        String url = "https://futapp.upallanovillavicencio.com/php/menu/insertar_liberar_mesa.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response.trim(), Toast.LENGTH_SHORT).show();
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
                params.put("numero_mesa", numeromesarece);
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }
    @Override
    public void onBackPressed() {
        if (!btnOrdenar.isEnabled()) {
            Toast.makeText(MenuCliente.this, "Espera la orden esta en proceso", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NO ORDENAR");
            builder.setMessage("¿Estás seguro de que deseas salir?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MenuCliente.this, Login.class);
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

}
