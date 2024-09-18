package com.example.cajero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    Button btnSalir;
    Button btnCerrarSesion;
    TextView textViewNombre, textViewEmail, textViewPhone, textViewCiudad, textViewNumeroCuenta, textViewIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        btnSalir = findViewById(R.id.btnSalir);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        textViewNombre = findViewById(R.id.textViewNombre);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewCiudad = findViewById(R.id.textViewCiudad);
        textViewIdNumber = findViewById(R.id.textViewNumber);

        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "Usuario");
        String userEmail = preferences.getString("user_email", "Sin correo");
        String userPhone = preferences.getString("user_phone", "Sin teléfono");
        String userCity = preferences.getString("user_city", "Sin ciudad");
        String userIdNumber = preferences.getString("user_id", "Sin ID");

        // Mostrar los datos del usuario en los TextViews
        textViewNombre.setText(userName);
        textViewEmail.setText("Correo Electrónico: " + userEmail);
        textViewPhone.setText("Número de Teléfono: " + userPhone);
        textViewCiudad.setText("Ciudad: " + userCity);
        textViewIdNumber.setText("Identificación: " + userIdNumber);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, MenuActivity.class));
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Elimina todos los datos de SharedPreferences
        editor.apply();

        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar el historial de actividades
            startActivity(intent);
            finish();  // Finalizar la actividad actual


        }
    }
