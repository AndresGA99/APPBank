package com.example.cajero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtNombre, txtCedula, txtTelefono, txtCiudad, txtCorreo, txtContraseña, txtContraseña2;
    private Button btnIngresar;
    private RequestQueue requestQueue;
    String url = "https://atm-api-eight.vercel.app/api/person"; // Nueva URL de la API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombre = findViewById(R.id.TxtNombre);
        txtCedula = findViewById(R.id.TxtidNumber);
        txtTelefono = findViewById(R.id.TxtPhone);
        txtCiudad = findViewById(R.id.TxtCiudad);
        txtCorreo = findViewById(R.id.TxtEmail);
        txtContraseña = findViewById(R.id.TxtPassword);
        txtContraseña2 = findViewById(R.id.TxtPassword2);
        btnIngresar = findViewById(R.id.btnIngresar);
        requestQueue = Volley.newRequestQueue(this);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        TextView textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        final String nombre = txtNombre.getText().toString().trim();
        final String cedula = txtCedula.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String ciudad = txtCiudad.getText().toString().trim();
        final String correo = txtCorreo.getText().toString().trim();
        final String contraseña = txtContraseña.getText().toString().trim();
        final String contraseña2 = txtContraseña2.getText().toString().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || cedula.isEmpty() || telefono.isEmpty() || ciudad.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de contraseñas coincidentes
        if (!contraseña.equals(contraseña2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", nombre);
            jsonParams.put("idNumber", cedula);
            jsonParams.put("email", correo);
            jsonParams.put("phone", telefono);
            jsonParams.put("adress", ciudad);
            jsonParams.put("password", contraseña);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String accountNumber = response.getString("accountNumber");

                                // Guardar los datos del usuario en SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_name", nombre);
                                editor.putString("user_email", correo);
                                editor.putString("user_phone", telefono);
                                editor.putString("user_city", ciudad);
                                editor.putString("user_id", cedula);

                                editor.apply();

                                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, ExitoActivity.class);
                                intent.putExtra("ACCOUNT_NUMBER", accountNumber);  // Pasar el número de cuenta
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null) {
                                String errorMessage = new String(error.networkResponse.data);
                                Toast.makeText(RegisterActivity.this, "Error al registrar: " + errorMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error de conexión o de la API", Toast.LENGTH_LONG).show();
                            }
                            error.printStackTrace();
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }

}
