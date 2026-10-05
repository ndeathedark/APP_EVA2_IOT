package com.example.webservicessencillo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo, etPassword;
    private Button btnIngresar, btnRegistrar, btnLimpiar;

    // IP estática de la Raspberry Pi en tu red WiFi
    private static final String IP_RASPBERRY = "http://169.254.139.30/iotdbpi/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relacionarVistas();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarPeticion(IP_RASPBERRY + "lg.php", true);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarPeticion(IP_RASPBERRY + "rg.php", false);
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCorreo.setText("");
                etPassword.setText("");
            }
        });
    }

    private void relacionarVistas() {
        etCorreo = findViewById(R.id.correo);
        etPassword = findViewById(R.id.password);
        btnIngresar = findViewById(R.id.acceso);
        btnRegistrar = findViewById(R.id.btnRegistro);
        btnLimpiar = findViewById(R.id.mostrar);
    }

    private void ejecutarPeticion(String url, final boolean esLogin) {
        final String correo = etCorreo.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res = response.trim();
                        if (esLogin) {
                            if (res.equals("exito")) {
                                Toast.makeText(getApplicationContext(), "Acceso Concedido", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (res.equals("registrado")) {
                                Toast.makeText(getApplicationContext(), "Usuario Registrado con Éxito", Toast.LENGTH_SHORT).show();
                            } else if (res.equals("existe")) {
                                Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String detalleError = "Error: ";
                        if (error.networkResponse != null) {
                            detalleError += "Código HTTP: " + error.networkResponse.statusCode;
                        } else if (error.getMessage() != null) {
                            detalleError += error.getMessage();
                        } else {
                            detalleError += error.toString();
                        }
                        Toast.makeText(getApplicationContext(), detalleError, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("email", correo);
                parametros.put("password", password);
                return parametros;
            }
        };

        // --- CONFIGURACIÓN DE TIMEOUT (10 SEGUNDOS) ---
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, // Tiempo de espera en milisegundos (10 segundos)
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Reintentos por defecto (1)
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}