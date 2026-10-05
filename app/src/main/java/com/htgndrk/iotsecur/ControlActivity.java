package com.htgndrk.iotsecur;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ControlActivity extends AppCompatActivity {

    private Button btnEncender, btnApagar;
    // IMPORTANTE: Pon la IP actual de tu Raspberry Pi y apunta a st.php
    private static final String URL_ESTADO = "http://10.63.55.194/ioteva2/st.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        btnEncender = findViewById(R.id.btnEncender);
        btnApagar = findViewById(R.id.btnApagar);

        // Eventos de los botones al ser presionados
        btnEncender.setOnClickListener(v -> actualizarSirena(1)); // 1 = Encendido
        btnApagar.setOnClickListener(v -> actualizarSirena(0));   // 0 = Apagado
    }

    private void actualizarSirena(int estadoSirena) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ESTADO,
                response -> {
                    // Si el st.php responde "actualizado", todo salió bien
                    if (response.trim().equals("actualizado")) {
                        Toast.makeText(ControlActivity.this, "¡Estado actualizado en AWS RDS!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ControlActivity.this, "Respuesta inesperada: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(ControlActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Estos parámetros coinciden exactamente con lo que espera tu st.php
                params.put("accion", "actualizar");
                params.put("sirena_encendida", String.valueOf(estadoSirena));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}