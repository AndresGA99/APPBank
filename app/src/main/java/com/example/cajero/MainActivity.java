package com.example.cajero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cajero.models.Account;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private RequestQueue requestQueue;
    String url = "https://atm-api-eight.vercel.app/api/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextCuenta);
        editTextPassword = findViewById(R.id.editTextPIN);
        buttonLogin = findViewById(R.id.buttonIngresar);

        requestQueue = Volley.newRequestQueue(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
    public void loginUser() {
        // Obteniendo los valores de nombre de usuario y contraseña desde los EditTexts
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        // Creando una solicitud POST usando StringRequest
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("accountNumber", username);
            jsonParams.put("password", password);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Mostrando el mensaje de respuesta en un Toast tkm


                        JSONObject name = response.getJSONObject("person");
                        double balance = response.getDouble("balance");
                        String accountNumber = response.getString("accountNumber");
                        Account account = new Account(accountNumber,balance); //hola
                        Toast.makeText(getApplicationContext(), "Bienvenido " + name.getString("name"), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        intent.putExtra("saldo", balance);
                        startActivity(intent);

                        finish();
                    } catch (JSONException ex) {
                        Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Mostrando el mensaje de error en un Toast
                    Toast.makeText(getApplicationContext(), "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException ex) {
            Toast.makeText(getApplicationContext(), "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }



}

