package com.example.cajero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cajero.models.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistorialActivity extends AppCompatActivity {
    private TransferAdapter adapter;
    private final ArrayList<String> transferList = new ArrayList<>();

    private static final String Urlbase = "https://atm-api-eight.vercel.app/api/transfer/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        String cuenta = Account.getInstance().getAccountNumber();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransferAdapter();
        recyclerView.setAdapter(adapter);
        String URL = Urlbase + cuenta;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // crear un bucle para los json de respuesta
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject transfer = response.getJSONObject(i);
                                String transferInfo = "Monto de la transaccion: " + transfer.getInt("amount") +
                                        "\nFecha: " + transfer.getString("createdAt") +
                                        "\nDestino de la transaccion: " + transfer.getString("destinationAccount");
                                transferList.add(transferInfo);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.TransferViewHolder> {

        class TransferViewHolder extends RecyclerView.ViewHolder {
            TextView transferInfo;

            TransferViewHolder(View itemView) {
                super(itemView);
                transferInfo = itemView.findViewById(R.id.transfer_info);
            }
        }

        @Override
        public TransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer, parent, false);
            return new TransferViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TransferViewHolder holder, int position) {
            holder.transferInfo.setText(transferList.get(position));
        }

        @Override
        public int getItemCount() {
            return transferList.size();
        }
    }
}
