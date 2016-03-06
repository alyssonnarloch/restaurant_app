package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.helper.SessionManager;
import com.app.narlocks.model.Order;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends DashboardActivity {

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setHeader(getString(R.string.app_name), true, true);
    }

    public void onNewOrderClick(View view) {
        SessionManager session = new SessionManager(getApplicationContext());
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("user_id", Integer.toString(session.getUserId()))
                .build();

        Request request = new Request.Builder()
                .url("http://52.36.228.76:8080/restaurant_service/webresources/order/new")
                .post(formBody)
                .header("Authorization", "Basic dHJ1dGFsb2NvQMOpbm9pem1hbm81Ng==")
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String connectionMessage = "";
                        if (!Extras.isNetworkAvailable(HomeActivity.this)) {
                            connectionMessage = "Verifique sua conexão com a internet.";
                        }
                        Toast.makeText(HomeActivity.this, "Erro ao iniciar o pedido. " + connectionMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Gson gson = new Gson();

                    order = gson.fromJson(result, Order.class);

                    Intent menuIntent = new Intent(HomeActivity.this, MenuActivity.class);
                    menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    menuIntent.putExtra("order", order);

                    startActivity(menuIntent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        Toast.makeText(HomeActivity.this, "Erro ao adicionar item ao pedido.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void onPaymentClick(View view) {
        Intent paymentIntent = new Intent(HomeActivity.this, PaymentActivity.class);
        startActivity(paymentIntent);
    }
}
