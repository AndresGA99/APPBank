package com.example.cajero;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MenuActivity extends AppCompatActivity {

    Button btnSalir;
    Button btnConsulta;
    Button btnRetiros;
    Button btnDeposito;
    Button btnTrans;
    Button btnPerfil;

    String url = "https://atm-api-eight.vercel.app/api/account/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnSalir = findViewById(R.id.btnSalir);
        btnConsulta= findViewById(R.id.btnconsulta);
        btnRetiros = findViewById(R.id.btnRetiros);
        btnDeposito = findViewById(R.id.btnDeposito);
        btnTrans = findViewById(R.id.btnTrans);
        btnPerfil = findViewById(R.id.btnPerfil);

        double balance = getIntent().getDoubleExtra("saldo", 0.0);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
        btnRetiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RetiroActivity.class);
                startActivity(intent);
            }
        });
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ConsultaActivity.class);
                startActivity(intent);
            }
        });

        btnDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, DepositoActivity.class));
            }
        });

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, TransferenciaActivity.class));
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, PerfilActivity.class));
            }
        });

    }

}
