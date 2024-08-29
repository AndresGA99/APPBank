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

public class TransferenciaActivity extends AppCompatActivity {

    private EditText txtMonto, txtCuenta;
    Account account = Account.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        txtMonto = findViewById(R.id.txtMonto);
        Button btnAcpetar = findViewById(R.id.btnAceptar);
        txtCuenta = findViewById(R.id.txtNumeroCuenta);

        btnAcpetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deposit();
            }
        });

    }

    private void deposit() {
        if(txtCuenta.getText().toString().isEmpty()){
            txtCuenta.setError("Por favor ingrese número de cuenta");
        }else if (txtMonto.getText().toString().isEmpty()) {
            txtMonto.setError("Por favaor ingrese monto");
        }else{
            final String originAccount = account.getAccountNumber();
            final String destinationAccount = txtCuenta.getText().toString();
            final float amount  = Float.parseFloat(txtMonto.getText().toString());
            try {
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("originAccount", originAccount);
                jsonParams.put("destinationAccount", destinationAccount);
                jsonParams.put("amount",amount );
                String url = "https://atm-api-eight.vercel.app/api/transfer/deposit";
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(TransferenciaActivity.this, "Se ha depositado al número de cuenta"+account, Toast.LENGTH_SHORT).show();
                        Account.getInstance().setMount(Account.getInstance().getMount()-amount);
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