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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cajero.models.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class ClaveActivity extends AppCompatActivity {
    private Button btnPass;

    private EditText idPassNueva, idPassAntigua;
    private RequestQueue requestQueue;
    String url ="https://atm-api-eight.vercel.app/api/user/updatePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clave);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPass = findViewById(R.id.btnPass);
        idPassNueva = findViewById(R.id.idPassNueva);
        idPassAntigua = findViewById(R.id.idPassAntigua);

        requestQueue = Volley.newRequestQueue(this);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });


    }

    public void changePass(){
        String idAccount = Account.getInstance().getAccountNumber();
        String newPass = idPassNueva.getText().toString();
        String oldPass = idPassAntigua.getText().toString();

        JSONObject parametros = new JSONObject();
        try {
            parametros.put("accountNumber", idAccount);
            parametros.put("newPassword", newPass);
            parametros.put("oldPassword", oldPass);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PATCH, url, parametros, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String mensaje = response.getString("message");
                        Toast.makeText(ClaveActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    }catch (JSONException e) {
                        Toast.makeText(ClaveActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
                    Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
}