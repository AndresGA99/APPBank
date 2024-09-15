package com.example.cajero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExitoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito);


        String accountNumber = getIntent().getStringExtra("ACCOUNT_NUMBER");
        TextView txtNumeroCuenta = findViewById(R.id.txtNumeroCuenta);
        txtNumeroCuenta.setText("Número de cuenta: " + accountNumber);

        Button btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(ExitoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}