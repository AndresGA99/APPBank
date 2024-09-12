package com.example.cajero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cajero.models.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class DepositoActivity extends AppCompatActivity {

    private EditText textDeposito;
    Account account = Account.getInstance();
    private Button btnD;
    private RequestQueue requestQueue;
    String url = "https://atm-api-eight.vercel.app/api/transaction/deposit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deposito);
        Button btnSalir = findViewById(R.id.btnSalir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textDeposito= findViewById(R.id.textDeposito);
        btnD = findViewById(R.id.btnD);

        requestQueue = Volley.newRequestQueue(this);

        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deposito();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepositoActivity.this, MenuActivity.class));
            }
        });
    }

    public void Deposito(){
        if(textDeposito.getText().toString().isEmpty()){
            textDeposito.setError("Por favor Ingrese monto");
        }else{
            final String accountNumber = account.getAccountNumber();
            final float amount  = Float.parseFloat(textDeposito.getText().toString());
            try {
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("accountNumber", accountNumber);
                jsonParams.put("amount",amount );
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Se ha depositado a la cuenta", Toast.LENGTH_SHORT).show();
                        Account.getInstance().setMount(amount+Account.getInstance().getMount());
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String responseBody = new String(volleyError.networkResponse.data);
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            Toast.makeText(getApplicationContext(), "Error al procesar la solicitud: "+ jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                Volley.newRequestQueue(this).add(stringRequest);
            }catch (JSONException ex){
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud:  "+ex, Toast.LENGTH_SHORT).show();
            }
        }
    }
}