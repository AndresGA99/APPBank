package com.example.cajero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cajero.models.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private RequestQueue requestQueue;

    // URL de la API para iniciar sesión
    private String url = "https://atm-api-eight.vercel.app/api/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar los componentes de la interfaz de usuario
        editTextUsername = findViewById(R.id.editTextCuenta);
        editTextPassword = findViewById(R.id.editTextPIN);
        buttonLogin = findViewById(R.id.btnIngresar);
        requestQueue = Volley.newRequestQueue(this);

        // Configurar el botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Configurar el enlace para ir a la pantalla de registro
        View textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        // Obtener valores de los EditTexts
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Crear parámetros JSON para la solicitud POST
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("accountNumber", username);
            jsonParams.put("password", password);

            // Crear la solicitud POST
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Procesar la respuesta de la API
                                JSONObject person = response.getJSONObject("person");
                                double balance = response.getDouble("balance");
                                String accountNumber = response.getString("accountNumber");

                                // Crear objeto Account y mostrar mensaje de bienvenida
                                Account account = new Account(accountNumber, balance);
                                Toast.makeText(getApplicationContext(), "Bienvenido " + person.getString("name"), Toast.LENGTH_SHORT).show();

                                // Iniciar la actividad del menú y pasar el saldo
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                intent.putExtra("saldo", balance);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error al procesar la respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Mostrar mensaje de error en caso de fallo
                            Toast.makeText(getApplicationContext(), "Error al procesar la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            // Añadir la solicitud a la cola de solicitudes
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
